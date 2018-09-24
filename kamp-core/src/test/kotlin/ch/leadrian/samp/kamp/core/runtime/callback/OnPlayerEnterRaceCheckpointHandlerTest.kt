package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterRaceCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerEnterRaceCheckpointHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEnterRaceCheckpointListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerEnterRaceCheckpointHandler = OnPlayerEnterRaceCheckpointHandler()
        onPlayerEnterRaceCheckpointHandler.register(listener1)
        onPlayerEnterRaceCheckpointHandler.register(listener2)
        onPlayerEnterRaceCheckpointHandler.register(listener3)

        onPlayerEnterRaceCheckpointHandler.onPlayerEnterRaceCheckpoint(player)

        verify(exactly = 1) {
            listener1.onPlayerEnterRaceCheckpoint(player)
            listener2.onPlayerEnterRaceCheckpoint(player)
            listener3.onPlayerEnterRaceCheckpoint(player)
        }
    }

}