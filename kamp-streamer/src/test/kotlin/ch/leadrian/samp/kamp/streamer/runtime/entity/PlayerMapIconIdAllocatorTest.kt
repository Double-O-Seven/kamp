package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class PlayerMapIconIdAllocatorTest {

    private lateinit var playerMapIconIdAllocator: PlayerMapIconIdAllocator
    private val callbackListenerManager: CallbackListenerManager = mockk()

    @BeforeEach
    fun setUp() {
        playerMapIconIdAllocator = PlayerMapIconIdAllocator(callbackListenerManager)
    }

    @Test
    fun shouldRegisterAsCallbackListener() {
        every { callbackListenerManager.register(any()) } just Runs

        playerMapIconIdAllocator.initialize()

        verify { callbackListenerManager.register(playerMapIconIdAllocator) }
    }

    @Test
    fun shouldInitializePlayerMapIconIdsOnPlayerConnect() {
        val player = mockk<Player>()

        playerMapIconIdAllocator.onPlayerConnect(player)

        val playerMapIconIds = (0..99).map { playerMapIconIdAllocator.allocate(player).playerMapIconId }.toList()
        assertThat(playerMapIconIds)
                .isEqualTo((0..99).map { PlayerMapIconId.valueOf(it) }.toList())
    }

    @Test
    fun givenAllPlayerMapIconIdsHaveBeenAllocatedItShouldThrowException() {
        val player = mockk<Player>()
        playerMapIconIdAllocator.onPlayerConnect(player)
        (0..99).map { playerMapIconIdAllocator.allocate(player).playerMapIconId }.toList()

        val caughtThrowable = catchThrowable { playerMapIconIdAllocator.allocate(player) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Failed to allocate player map icon ID")
    }

    @Test
    fun shouldAllocateIndependentlyOfOtherPlayers() {
        val player1 = mockk<Player>()
        val player2 = mockk<Player>()
        playerMapIconIdAllocator.onPlayerConnect(player1)
        playerMapIconIdAllocator.onPlayerConnect(player2)

        val allocation1 = playerMapIconIdAllocator.allocate(player1)
        val allocation2 = playerMapIconIdAllocator.allocate(player2)

        assertAll(
                { assertThat(allocation1.playerMapIconId).isEqualTo(PlayerMapIconId.valueOf(0)) },
                { assertThat(allocation2.playerMapIconId).isEqualTo(PlayerMapIconId.valueOf(0)) }
        )
    }

    @Test
    fun onPlayerDisconnectShouldClearAllPlayerMapIconIdsForPlayer() {
        val player = mockk<Player>()
        playerMapIconIdAllocator.onPlayerConnect(player)

        playerMapIconIdAllocator.onPlayerDisconnect(player, DisconnectReason.QUIT)
        val caughtThrowable = catchThrowable { playerMapIconIdAllocator.allocate(player) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Failed to allocate player map icon ID")
    }

    @Test
    fun shouldReleasePlayerMapIconId() {
        val player = mockk<Player>()
        playerMapIconIdAllocator.onPlayerConnect(player)
        val allocation = playerMapIconIdAllocator.allocate(player)

        allocation.release()

        assertThat(playerMapIconIdAllocator.allocate(player).playerMapIconId)
                .isEqualTo(allocation.playerMapIconId)
    }

    @Test
    fun givenAllocationWasAlreadyReleasedItShouldThrowAnException() {
        val player = mockk<Player>()
        playerMapIconIdAllocator.onPlayerConnect(player)
        val allocation = playerMapIconIdAllocator.allocate(player)
        allocation.release()

        val caughtThrowable = catchThrowable { allocation.release() }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Player map icon ID 0 was already released")
    }
}