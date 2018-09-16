package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.exception.PlayerOfflineException
import io.mockk.Runs
import io.mockk.every
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PlayerSearchIndexTest {

    private val playerSearchIndex = PlayerSearchIndex()

    @Test
    fun shouldIndexPlayer() {
        val player = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "hans.wurst"
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }

        playerSearchIndex.index(player)

        assertThat(playerSearchIndex.getPlayer("hans.wurst"))
                .isSameAs(player)
    }

    @Test
    fun shouldIndexPlayerCaseInsensitive() {
        val player = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "Hans.WURST"
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }

        playerSearchIndex.index(player)

        assertThat(playerSearchIndex.getPlayer("hans.wurst"))
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
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        val player2 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "john.sausage"
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        val player3 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "Hanspeter"
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        playerSearchIndex.apply {
            index(player1)
            index(player2)
            index(player3)
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
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        val player2 = mockk<Player> {
            every { requireConnected() } returns this
            every { name } returns "john.sausage"
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        playerSearchIndex.apply {
            index(player1)
            index(player2)
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
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        playerSearchIndex.index(player)
        val slot = slot<Player.(DisconnectReason) -> Unit>()
        verify { player.onDisconnect(capture(slot)) }

        slot.invoke(player, DisconnectReason.QUIT)

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
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        playerSearchIndex.index(player)
        val slot = slot<Player.(String, String) -> Unit>()
        verify { player.onNameChange(capture(slot)) }

        slot.invoke(player, oldName, newName)

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
            every { onNameChange(any()) } just Runs
            every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
        }
        playerSearchIndex.index(player)

        val caughtThrowable = catchThrowable { playerSearchIndex.index(player) }

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
                every { onNameChange(any()) } just Runs
                every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
            }

            playerSearchIndex.index(player)

            verify { player.requireConnected() }
        }

        @Test
        fun givenRequireOnlineIsViolatedItShouldNotIndexPlayer() {
            val playerName = "hans.wurst"
            val player = mockk<Player> {
                every { requireConnected() } throws PlayerOfflineException("test")
                every { name } returns playerName
                every { onNameChange(any()) } just Runs
                every { onDisconnect(any<Player.(DisconnectReason) -> Unit>()) } just Runs
            }

            catchThrowable { playerSearchIndex.index(player) }

            assertThat(playerSearchIndex.getPlayer(playerName))
                    .isNull()
        }

    }

}