package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterCheckpointListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerEnterCheckpointHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEnterCheckpointListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerEnterCheckpointHandler = OnPlayerEnterCheckpointHandler()
        onPlayerEnterCheckpointHandler.register(listener1)
        onPlayerEnterCheckpointHandler.register(listener2)
        onPlayerEnterCheckpointHandler.register(listener3)

        onPlayerEnterCheckpointHandler.onPlayerEnterCheckpoint(player)

        verify(exactly = 1) {
            listener1.onPlayerEnterCheckpoint(player)
            listener2.onPlayerEnterCheckpoint(player)
            listener3.onPlayerEnterCheckpoint(player)
        }
    }

}