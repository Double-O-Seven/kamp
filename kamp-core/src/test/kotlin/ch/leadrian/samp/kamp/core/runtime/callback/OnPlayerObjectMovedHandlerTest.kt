package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerObjectMovedListener
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerObjectMovedHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerObjectMovedListener>(relaxed = true)
        val listener2 = mockk<OnPlayerObjectMovedListener>(relaxed = true)
        val listener3 = mockk<OnPlayerObjectMovedListener>(relaxed = true)
        val playerMapObject = mockk<PlayerMapObject>()
        val onPlayerMapObjectObjectMovedHandler = OnPlayerObjectMovedHandler()
        onPlayerMapObjectObjectMovedHandler.register(listener1)
        onPlayerMapObjectObjectMovedHandler.register(listener2)
        onPlayerMapObjectObjectMovedHandler.register(listener3)

        onPlayerMapObjectObjectMovedHandler.onPlayerObjectMoved(playerMapObject)

        verify(exactly = 1) {
            listener1.onPlayerObjectMoved(playerMapObject)
            listener2.onPlayerObjectMoved(playerMapObject)
            listener3.onPlayerObjectMoved(playerMapObject)
        }
    }

}