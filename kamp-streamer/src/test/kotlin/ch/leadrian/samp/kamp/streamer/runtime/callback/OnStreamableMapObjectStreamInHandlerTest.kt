package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamInListener
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnStreamableMapObjectStreamInHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnStreamableMapObjectStreamInListener>(relaxed = true)
        val listener2 = mockk<OnStreamableMapObjectStreamInListener>(relaxed = true)
        val listener3 = mockk<OnStreamableMapObjectStreamInListener>(relaxed = true)
        val streamableMapObject = mockk<StreamableMapObject>()
        val forPlayer = mockk<Player>()
        val onStreamableMapObjectStreamInHandler = OnStreamableMapObjectStreamInHandler()
        onStreamableMapObjectStreamInHandler.register(listener1)
        onStreamableMapObjectStreamInHandler.register(listener2)
        onStreamableMapObjectStreamInHandler.register(listener3)

        onStreamableMapObjectStreamInHandler.onStreamableMapObjectStreamIn(streamableMapObject = streamableMapObject, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onStreamableMapObjectStreamIn(streamableMapObject = streamableMapObject, forPlayer = forPlayer)
            listener2.onStreamableMapObjectStreamIn(streamableMapObject = streamableMapObject, forPlayer = forPlayer)
            listener3.onStreamableMapObjectStreamIn(streamableMapObject = streamableMapObject, forPlayer = forPlayer)
        }
    }

}