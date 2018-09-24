package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerLeaveRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerLeaveRaceCheckpointHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
        val listener2 = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
        val listener3 = mockk<OnPlayerLeaveRaceCheckpointListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerLeaveRaceCheckpointHandler = OnPlayerLeaveRaceCheckpointHandler()
        onPlayerLeaveRaceCheckpointHandler.register(listener1)
        onPlayerLeaveRaceCheckpointHandler.register(listener2)
        onPlayerLeaveRaceCheckpointHandler.register(listener3)

        onPlayerLeaveRaceCheckpointHandler.onPlayerLeaveRaceCheckpoint(player)

        verify(exactly = 1) {
            listener1.onPlayerLeaveRaceCheckpoint(player)
            listener2.onPlayerLeaveRaceCheckpoint(player)
            listener3.onPlayerLeaveRaceCheckpoint(player)
        }
    }

}