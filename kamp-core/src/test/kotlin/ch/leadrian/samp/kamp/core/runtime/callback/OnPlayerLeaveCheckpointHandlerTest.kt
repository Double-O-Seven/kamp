package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerLeaveCheckpointHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
        val listener2 = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
        val listener3 = mockk<OnPlayerLeaveCheckpointListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerLeaveCheckpointHandler = OnPlayerLeaveCheckpointHandler()
        onPlayerLeaveCheckpointHandler.register(listener1)
        onPlayerLeaveCheckpointHandler.register(listener2)
        onPlayerLeaveCheckpointHandler.register(listener3)

        onPlayerLeaveCheckpointHandler.onPlayerLeaveCheckpoint(player)

        verify(exactly = 1) {
            listener1.onPlayerLeaveCheckpoint(player)
            listener2.onPlayerLeaveCheckpoint(player)
            listener3.onPlayerLeaveCheckpoint(player)
        }
    }

}