package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnMapObjectMovedListener
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnMapObjectMovedHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnMapObjectMovedListener>(relaxed = true)
        val listener2 = mockk<OnMapObjectMovedListener>(relaxed = true)
        val listener3 = mockk<OnMapObjectMovedListener>(relaxed = true)
        val mapObject = mockk<MapObject>()
        val onObjectMovedHandler = OnMapObjectMovedHandler()
        onObjectMovedHandler.register(listener1)
        onObjectMovedHandler.register(listener2)
        onObjectMovedHandler.register(listener3)

        onObjectMovedHandler.onMapObjectMoved(mapObject)

        verify(exactly = 1) {
            listener1.onMapObjectMoved(mapObject)
            listener2.onMapObjectMoved(mapObject)
            listener3.onMapObjectMoved(mapObject)
        }
    }

}