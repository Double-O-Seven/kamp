package ch.leadrian.samp.kamp.streamer.runtime.callback

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerSelectStreamableMapObjectListener
import ch.leadrian.samp.kamp.streamer.runtime.entity.StreamableMapObjectImpl
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerSelectStreamableMapObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerSelectStreamableMapObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerSelectStreamableMapObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerSelectStreamableMapObjectListener>(relaxed = true)
        val player = mockk<Player>()
        val streamableMapObject = mockk<StreamableMapObjectImpl>()
        val modelId = 69
        val coordinates = vector3DOf(1f, 2f, 3f)
        val onPlayerSelectStreamableMapObjectHandler = OnPlayerSelectStreamableMapObjectHandler()
        onPlayerSelectStreamableMapObjectHandler.register(listener1)
        onPlayerSelectStreamableMapObjectHandler.register(listener2)
        onPlayerSelectStreamableMapObjectHandler.register(listener3)

        onPlayerSelectStreamableMapObjectHandler.onPlayerSelectStreamableMapObject(player, streamableMapObject, modelId, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerSelectStreamableMapObject(player, streamableMapObject, modelId, coordinates)
            listener2.onPlayerSelectStreamableMapObject(player, streamableMapObject, modelId, coordinates)
            listener3.onPlayerSelectStreamableMapObject(player, streamableMapObject, modelId, coordinates)
        }
    }

}