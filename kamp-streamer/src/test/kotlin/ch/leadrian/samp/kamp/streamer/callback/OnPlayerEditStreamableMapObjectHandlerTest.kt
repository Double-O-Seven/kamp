package ch.leadrian.samp.kamp.streamer.callback

import ch.leadrian.samp.kamp.core.api.constants.ObjectEditResponse
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.entity.StreamableMapObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerEditStreamableStreamableMapObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerEditStreamableMapObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEditStreamableMapObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEditStreamableMapObjectListener>(relaxed = true)
        val player = mockk<Player>()
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        val response = ObjectEditResponse.FINAL
        val streamableMapObject = mockk<StreamableMapObject>()
        val onPlayerEditStreamableMapObjectHandler = OnPlayerEditStreamableMapObjectHandler()
        onPlayerEditStreamableMapObjectHandler.register(listener1)
        onPlayerEditStreamableMapObjectHandler.register(listener2)
        onPlayerEditStreamableMapObjectHandler.register(listener3)

        onPlayerEditStreamableMapObjectHandler.onPlayerEditStreamableMapObject(
                player = player,
                offset = offset,
                rotation = rotation,
                response = response,
                streamableMapObject = streamableMapObject
        )

        verify(exactly = 1) {
            listener1.onPlayerEditStreamableMapObject(
                    player = player,
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    streamableMapObject = streamableMapObject
            )
            listener2.onPlayerEditStreamableMapObject(
                    player = player,
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    streamableMapObject = streamableMapObject
            )
            listener3.onPlayerEditStreamableMapObject(
                    player = player,
                    offset = offset,
                    rotation = rotation,
                    response = response,
                    streamableMapObject = streamableMapObject
            )
        }
    }

}