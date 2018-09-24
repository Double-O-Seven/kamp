package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditAttachedObjectListener
import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerEditAttachedObjectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerEditAttachedObjectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerEditAttachedObjectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerEditAttachedObjectListener>(relaxed = true)
        val player = mockk<Player>()
        val slot = mockk<AttachedObjectSlot>()
        val rotation = vector3DOf(1f, 2f, 3f)
        val offset = vector3DOf(4f, 5f, 6f)
        val response = AttachedObjectEditResponse.CANCEL
        val bone = Bone.CLAVICLE_LEFT
        val scale = vector3DOf(7f, 8f, 9f)
        val onPlayerEditAttachedObjectHandler = OnPlayerEditAttachedObjectHandler()
        onPlayerEditAttachedObjectHandler.register(listener1)
        onPlayerEditAttachedObjectHandler.register(listener2)
        onPlayerEditAttachedObjectHandler.register(listener3)

        onPlayerEditAttachedObjectHandler.onPlayerEditAttachedObject(
                player = player,
                slot = slot,
                modelId = 1337,
                rotation = rotation,
                offset = offset,
                response = response,
                bone = bone,
                scale = scale
        )

        verify(exactly = 1) {
            listener1.onPlayerEditAttachedObject(
                    player = player,
                    slot = slot,
                    modelId = 1337,
                    rotation = rotation,
                    offset = offset,
                    response = response,
                    bone = bone,
                    scale = scale
            )
            listener2.onPlayerEditAttachedObject(
                    player = player,
                    slot = slot,
                    modelId = 1337,
                    rotation = rotation,
                    offset = offset,
                    response = response,
                    bone = bone,
                    scale = scale
            )
            listener3.onPlayerEditAttachedObject(
                    player = player,
                    slot = slot,
                    modelId = 1337,
                    rotation = rotation,
                    offset = offset,
                    response = response,
                    bone = bone,
                    scale = scale
            )
        }
    }

}