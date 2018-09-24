package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnObjectMovedListener
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnObjectMovedHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnObjectMovedListener>(relaxed = true)
        val listener2 = mockk<OnObjectMovedListener>(relaxed = true)
        val listener3 = mockk<OnObjectMovedListener>(relaxed = true)
        val mapObject = mockk<MapObject>()
        val onObjectMovedHandler = OnObjectMovedHandler()
        onObjectMovedHandler.register(listener1)
        onObjectMovedHandler.register(listener2)
        onObjectMovedHandler.register(listener3)

        onObjectMovedHandler.onObjectMoved(mapObject)

        verify(exactly = 1) {
            listener1.onObjectMoved(mapObject)
            listener2.onObjectMoved(mapObject)
            listener3.onObjectMoved(mapObject)
        }
    }

}