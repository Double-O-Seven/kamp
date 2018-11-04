package ch.leadrian.samp.kamp.streamer.api.callback

import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnStreamableMapObjectMovedHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnStreamableMapObjectMovedListener>(relaxed = true)
        val listener2 = mockk<OnStreamableMapObjectMovedListener>(relaxed = true)
        val listener3 = mockk<OnStreamableMapObjectMovedListener>(relaxed = true)
        val streamableMapObject = mockk<StreamableMapObject>()
        val onStreamableMapObjectObjectMovedHandler = OnStreamableMapObjectMovedHandler()
        onStreamableMapObjectObjectMovedHandler.register(listener1)
        onStreamableMapObjectObjectMovedHandler.register(listener2)
        onStreamableMapObjectObjectMovedHandler.register(listener3)

        onStreamableMapObjectObjectMovedHandler.onStreamableMapObjectMoved(streamableMapObject)

        verify(exactly = 1) {
            listener1.onStreamableMapObjectMoved(streamableMapObject)
            listener2.onStreamableMapObjectMoved(streamableMapObject)
            listener3.onStreamableMapObjectMoved(streamableMapObject)
        }
    }

}