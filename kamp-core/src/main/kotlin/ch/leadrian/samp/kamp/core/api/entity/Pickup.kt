package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupReceiver
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.core.api.entity.extension.Extendable
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerPickUpPickupReceiverDelegate

class Pickup
internal constructor(
        val modelId: Int,
        coordinates: Vector3D,
        val type: Int,
        val virtualWorldId: Int?,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerPickUpPickupReceiver: OnPlayerPickUpPickupReceiverDelegate = OnPlayerPickUpPickupReceiverDelegate()
) : Entity<PickupId>,
        AbstractDestroyable(),
        Extendable<Pickup>,
        OnPlayerPickUpPickupReceiver by onPlayerPickUpPickupReceiver {

    override val extensions: EntityExtensionContainer<Pickup> = EntityExtensionContainer(this)

    override val id: PickupId
        get() = requireNotDestroyed { field }

    init {
        val pickupId = nativeFunctionExecutor.createPickup(
                model = modelId,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                type = type,
                virtualworld = virtualWorldId ?: -1
        )

        if (pickupId == PickupId.INVALID.value) {
            throw CreationFailedException("Could not create pickup")
        }

        id = PickupId.valueOf(pickupId)
    }

    val coordinates: Vector3D = coordinates.toVector3D()

    internal fun onPickUp(player: Player) {
        onPlayerPickUpPickupReceiver.onPlayerPickUpPickup(player, this)
    }

    override fun onDestroy() {
        extensions.destroy()
        nativeFunctionExecutor.destroyPickup(id.value)
    }

}