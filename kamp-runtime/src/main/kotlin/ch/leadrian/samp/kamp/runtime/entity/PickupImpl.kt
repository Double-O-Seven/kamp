package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Pickup
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.id.PickupId
import ch.leadrian.samp.kamp.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor

internal class PickupImpl(
        override val modelId: Int,
        coordinates: Vector3D,
        override val type: Int,
        override val virtualWorldId: Int?,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Pickup {

    private val onPickUpHandlers: MutableList<Pickup.(Player) -> Unit> = mutableListOf()

    private val onDestroyHandlers: MutableList<PickupImpl.() -> Unit> = mutableListOf()

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

    override val coordinates: Vector3D = coordinates.toVector3D()

    override fun onPickUp(onPickUp: Pickup.(Player) -> Unit) {
        onPickUpHandlers += onPickUp
    }

    internal fun onPickUp(player: Player) {
        onPickUpHandlers.forEach { it.invoke(this, player) }
    }

    internal fun onDestroy(onDestroy: PickupImpl.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.destroyPickup(id.value)
        isDestroyed = true
    }

}