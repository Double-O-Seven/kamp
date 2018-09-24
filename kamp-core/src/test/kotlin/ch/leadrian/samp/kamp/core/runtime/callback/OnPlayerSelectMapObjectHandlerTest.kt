package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectMapObjectListener
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerSelectMapObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerSelectMapObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerSelectMapObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerSelectMapObjectListener>(relaxed = true)
        val player = mockk<Player>()
        val mapObject = mockk<MapObject>()
        val modelId = 69
        val coordinates = vector3DOf(1f, 2f, 3f)
        val onPlayerSelectMapObjectHandler = OnPlayerSelectMapObjectHandler()
        onPlayerSelectMapObjectHandler.register(listener1)
        onPlayerSelectMapObjectHandler.register(listener2)
        onPlayerSelectMapObjectHandler.register(listener3)

        onPlayerSelectMapObjectHandler.onPlayerSelectMapObject(player, mapObject, modelId, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerSelectMapObject(player, mapObject, modelId, coordinates)
            listener2.onPlayerSelectMapObject(player, mapObject, modelId, coordinates)
            listener3.onPlayerSelectMapObject(player, mapObject, modelId, coordinates)
        }
    }

}