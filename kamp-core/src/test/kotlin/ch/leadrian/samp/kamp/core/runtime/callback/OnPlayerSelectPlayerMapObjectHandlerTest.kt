package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerSelectPlayerMapObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerSelectPlayerMapObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerSelectPlayerMapObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerSelectPlayerMapObjectListener>(relaxed = true)
        val player = mockk<Player>()
        val playerMapObject = mockk<PlayerMapObject>()
        val modelId = 69
        val coordinates = vector3DOf(1f, 2f, 3f)
        val onPlayerSelectPlayerMapObjectHandler = OnPlayerSelectPlayerMapObjectHandler()
        onPlayerSelectPlayerMapObjectHandler.register(listener1)
        onPlayerSelectPlayerMapObjectHandler.register(listener2)
        onPlayerSelectPlayerMapObjectHandler.register(listener3)

        onPlayerSelectPlayerMapObjectHandler.onPlayerSelectPlayerMapObject(player, playerMapObject, modelId, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerSelectPlayerMapObject(player, playerMapObject, modelId, coordinates)
            listener2.onPlayerSelectPlayerMapObject(player, playerMapObject, modelId, coordinates)
            listener3.onPlayerSelectPlayerMapObject(player, playerMapObject, modelId, coordinates)
        }
    }

}