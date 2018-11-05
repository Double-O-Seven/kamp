package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnStreamableMapObjectStreamOutListener
import ch.leadrian.samp.kamp.streamer.api.entity.StreamableMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnStreamableMapObjectStreamOutHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnStreamableMapObjectStreamOutListener>(relaxed = true)
        val listener2 = mockk<OnStreamableMapObjectStreamOutListener>(relaxed = true)
        val listener3 = mockk<OnStreamableMapObjectStreamOutListener>(relaxed = true)
        val streamableMapObject = mockk<StreamableMapObject>()
        val forPlayer = mockk<Player>()
        val onStreamableMapObjectStreamOutHandler = OnStreamableMapObjectStreamOutHandler()
        onStreamableMapObjectStreamOutHandler.register(listener1)
        onStreamableMapObjectStreamOutHandler.register(listener2)
        onStreamableMapObjectStreamOutHandler.register(listener3)

        onStreamableMapObjectStreamOutHandler.onStreamableMapObjectStreamOut(streamableMapObject = streamableMapObject, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onStreamableMapObjectStreamOut(streamableMapObject = streamableMapObject, forPlayer = forPlayer)
            listener2.onStreamableMapObjectStreamOut(streamableMapObject = streamableMapObject, forPlayer = forPlayer)
            listener3.onStreamableMapObjectStreamOut(streamableMapObject = streamableMapObject, forPlayer = forPlayer)
        }
    }

}