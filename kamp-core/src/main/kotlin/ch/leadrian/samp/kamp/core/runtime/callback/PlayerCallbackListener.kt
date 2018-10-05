package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDeathListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEditAttachedObjectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.Priority
import ch.leadrian.samp.kamp.core.api.constants.AttachedObjectEditResponse
import ch.leadrian.samp.kamp.core.api.constants.Bone
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.AttachedObjectSlot
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Priority(value = Int.MIN_VALUE, listenerClass = OnPlayerDisconnectListener::class)
@Singleton
internal class PlayerCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnPlayerSpawnListener, OnPlayerDeathListener, OnPlayerDisconnectListener, OnPlayerEditAttachedObjectListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerSpawn(player: Player) {
        player.onSpawn()
    }

    override fun onPlayerDeath(player: Player, killer: Player?, reason: WeaponModel) {
        player.onDeath(killer, reason)
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        player.onDisconnect(reason)
    }

    override fun onPlayerEditAttachedObject(
            player: Player,
            slot: AttachedObjectSlot,
            response: AttachedObjectEditResponse,
            modelId: Int,
            bone: Bone,
            offset: Vector3D,
            rotation: Vector3D,
            scale: Vector3D
    ) {
        slot.onEdit(response, modelId, bone, offset, rotation, scale)
    }

}