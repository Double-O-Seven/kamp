package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.MapObject
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerEditMapObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerEditMapObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEditMapObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEditMapObjectListener>(relaxed = true)
        val player = mockk<Player>()
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        val response = ObjectEditResponse.FINAL
        val mapObject = mockk<MapObject>()
        val onPlayerEditMapObjectHandler = OnPlayerEditMapObjectHandler()
        onPlayerEditMapObjectHandler.register(listener1)
        onPlayerEditMapObjectHandler.register(listener2)
        onPlayerEditMapObjectHandler.register(listener3)

        onPlayerEditMapObjectHandler.onPlayerEditMapObject(
                player = player,
                offset = offset,
                rotation = rotation,
                response = response,
                mapObject = mapObject
        )

        verify(exactly = 1) {
            listener1.onPlayerEditMapObject(
                    player = player,
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    mapObject = mapObject
            )
            listener2.onPlayerEditMapObject(
                    player = player,
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    mapObject = mapObject
            )
            listener3.onPlayerEditMapObject(
                    player = player,
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    mapObject = mapObject
            )
        }
    }

}