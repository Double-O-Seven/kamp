package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class Pickup
internal constructor(
        val modelId: Int,
        coordinates: Vector3D,
        val type: Int,
        val virtualWorldId: Int?,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<PickupId>, AbstractDestroyable() {

    private val onPickUpHandlers: MutableList<Pickup.(Player) -> Unit> = mutableListOf()

    private val onDestroyHandlers: MutableList<Pickup.() -> Unit> = mutableListOf()

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

    fun onPickUp(onPickUp: Pickup.(Player) -> Unit) {
        onPickUpHandlers += onPickUp
    }

    internal fun onPickUp(player: Player) {
        onPickUpHandlers.forEach { it.invoke(this, player) }
    }

    fun onDestroy(onDestroy: Pickup.() -> Unit) {
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