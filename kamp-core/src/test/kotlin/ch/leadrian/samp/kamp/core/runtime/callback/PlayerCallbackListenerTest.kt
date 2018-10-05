package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerCallbackListenerTest {

    private lateinit var playerCallbackListener: PlayerCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        playerCallbackListener = PlayerCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        playerCallbackListener.initialize()

        verify { callbackListenerManager.register(playerCallbackListener) }
    }

    @Test
    fun shouldCallOnSpawn() {
        val player = mockk<Player> {
            every { onSpawn() } just Runs
        }

        playerCallbackListener.onPlayerSpawn(player)

        verify { player.onSpawn() }
    }

    @Test
    fun shouldCallOnDeath() {
        val player = mockk<Player> {
            every { onDeath(any(), any()) } just Runs
        }
        val killer = mockk<Player>()

        playerCallbackListener.onPlayerDeath(player, killer, WeaponModel.TEC9)

        verify { player.onDeath(killer, WeaponModel.TEC9) }
    }

    @Test
    fun shouldCallOnDisconnect() {
        val player = mockk<Player> {
            every { onDisconnect(any<DisconnectReason>()) } just Runs
        }

        playerCallbackListener.onPlayerDisconnect(player, DisconnectReason.QUIT)

        verify { player.onDisconnect(DisconnectReason.QUIT) }
    }

    @Test
    fun shouldCallOnEditForAttachObjectSlot() {
        val offset = vector3DOf(1f, 2f, 3f)
        val rotation = vector3DOf(4f, 5f, 6f)
        val scale = vector3DOf(7f, 8f, 9f)
        val slot = mockk<AttachedObjectSlot> {
            every { onEdit(any(), any(), any(), any(), any(), any()) } just Runs
        }
        val player = mockk<Player>()

        playerCallbackListener.onPlayerEditAttachedObject(
                player = player,
                slot = slot,
                response = AttachedObjectEditResponse.SAVE,
                modelId = 1337,
                bone = Bone.HEAD,
                offset = offset,
                rotation = rotation,
                scale = scale
        )

        verify {
            slot.onEdit(
                    response = AttachedObjectEditResponse.SAVE,
                    modelId = 1337,
                    bone = Bone.HEAD,
                    offset = offset,
                    rotation = rotation,
                    scale = scale
            )
        }
    }

}