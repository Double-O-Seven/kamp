package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.exception.PlayerOfflineException
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PlayerSearchIndexTest {

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private lateinit var playerSearchIndex: PlayerSearchIndex

    @BeforeEach
    fun setUp() {
        playerSearchIndex = PlayerSearchIndex(callbackListenerManager)
    }

    @Test
    fun initializeShouldCallCallbackListenerManager() {
        every { callbackListenerManager.register(any()) } just Runs

        playerSearchIndex.initialize()

        verify { callbackListenerManager.register(playerSearchIndex) }
    }

    @ParameterizedTest
    @CsvSource(
            "hans.wurst, hans.wurst",
            "Hans.Wurst, hans.wurst",
            "hans.wurst, HANS.WURST"
    )
    fun shouldIndexPlayerCaseInsensitive(playerName: String, searchName: String) {
        val player = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns playerName
        }

        playerSearchIndex.onPlayerConnect(player)

        assertThat(playerSearchIndex.getPlayer(searchName))
                .isSameAs(player)
    }

    @Test
    fun givenNotIndexedNameItShouldReturnNull() {
        val indexedPlayer = playerSearchIndex.getPlayer("hans.wurst")

        assertThat(indexedPlayer)
                .isNull()
    }

    @Test
    fun shouldFindAllPlayersWithMatchingPrefix() {
        val player1 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "hans.wurst"
        }
        val player2 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "john.sausage"
        }
        val player3 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "Hanspeter"
        }
        playerSearchIndex.apply {
            onPlayerConnect(player1)
            onPlayerConnect(player2)
            onPlayerConnect(player3)
        }

        val players = playerSearchIndex.findPlayers("hans")

        assertThat(players)
                .containsExactlyInAnyOrder(player1, player3)
    }

    @Test
    fun givenNotMatchingPrefixItShouldReturnEmptyList() {
        val player1 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "hans.wurst"
        }
        val player2 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "john.sausage"
        }
        playerSearchIndex.apply {
            onPlayerConnect(player1)
            onPlayerConnect(player2)
        }

        val players = playerSearchIndex.findPlayers("lol")

        assertThat(players)
                .isEmpty()
    }

    @Test
    fun onDisconnectShouldRemovePlayerFromIndex() {
        val playerName = "hans.wurst"
        val player = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns playerName
        }
        playerSearchIndex.onPlayerConnect(player)

        playerSearchIndex.onPlayerDisconnect(player, DisconnectReason.QUIT)

        assertThat(playerSearchIndex.getPlayer(playerName))
                .isNull()
    }


    @Test
    fun onNameChangeShouldReindexPlayer() {
        val oldName = "hans.wurst"
        val newName = "john.sausage"
        val player = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns oldName
        }
        playerSearchIndex.onPlayerConnect(player)

        playerSearchIndex.onPlayerNameChange(player, oldName, newName)

        assertThat(playerSearchIndex.getPlayer(oldName))
                .isNull()
        assertThat(playerSearchIndex.getPlayer(newName))
                .isSameAs(player)
    }

    @Test
    fun givenSameNameIsIndexedTwiceItShouldThrowException() {
        val player = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "hans.wurst"
        }
        playerSearchIndex.onPlayerConnect(player)

        val caughtThrowable = catchThrowable { playerSearchIndex.onPlayerConnect(player) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Nested
    inner class RequireOnlineTests {

        @Test
        fun shouldRequirePlayerToBeOnline() {
            val player = mockk<Player> {
                every { requireConnected() } returns this
                every { name } returns "hans.wurst"
            }

            playerSearchIndex.onPlayerConnect(player)

            verify { player.requireConnected() }
        }

        @Test
        fun givenRequireOnlineIsViolatedItShouldNotIndexPlayer() {
            val playerName = "hans.wurst"
            val player = mockk<Player> {
                every { requireConnected() } throws PlayerOfflineException("test")
                every { name } returns playerName
            }

            catchThrowable { playerSearchIndex.onPlayerConnect(player) }

            assertThat(playerSearchIndex.getPlayer(playerName))
                    .isNull()
        }

    }

}