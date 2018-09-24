package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditPlayerMapObjectListener
import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerEditPlayerPlayerMapObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerEditPlayerMapObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEditPlayerMapObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEditPlayerMapObjectListener>(relaxed = true)
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        val response = ObjectEditResponse.FINAL
        val playerMapObject = mockk<PlayerMapObject>()
        val onPlayerEditPlayerMapObjectHandler = OnPlayerEditPlayerMapObjectHandler()
        onPlayerEditPlayerMapObjectHandler.register(listener1)
        onPlayerEditPlayerMapObjectHandler.register(listener2)
        onPlayerEditPlayerMapObjectHandler.register(listener3)

        onPlayerEditPlayerMapObjectHandler.onPlayerEditPlayerMapObject(
                offset = offset,
                rotation = rotation,
                response = response,
                playerMapObject = playerMapObject
        )

        verify(exactly = 1) {
            listener1.onPlayerEditPlayerMapObject(
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    playerMapObject = playerMapObject
            )
            listener2.onPlayerEditPlayerMapObject(
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    playerMapObject = playerMapObject
            )
            listener3.onPlayerEditPlayerMapObject(
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    playerMapObject = playerMapObject
            )
        }
    }

}