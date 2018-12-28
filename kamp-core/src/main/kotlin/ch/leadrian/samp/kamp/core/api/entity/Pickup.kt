package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerPickUpPickupListener
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

    private val onPlayerPickUpPickupListeners = LinkedHashSet<OnPlayerPickUpPickupListener>()

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

    fun addOnPlayerPickUpPickupListener(listener: OnPlayerPickUpPickupListener) {
        onPlayerPickUpPickupListeners += listener
    }

    fun removeOnPlayerPickUpPickupListener(listener: OnPlayerPickUpPickupListener) {
        onPlayerPickUpPickupListeners -= listener
    }

    inline fun onPickUp(crossinline onPickUp: Pickup.(Player) -> Unit): OnPlayerPickUpPickupListener {
        val listener = object : OnPlayerPickUpPickupListener {

            override fun onPlayerPickUpPickup(player: Player, pickup: Pickup) {
                onPickUp.invoke(pickup, player)
            }
        }
        addOnPlayerPickUpPickupListener(listener)
        return listener
    }

    internal fun onPickUp(player: Player) {
        onPlayerPickUpPickupListeners.forEach { it.onPlayerPickUpPickup(player, this) }
    }

    override fun onDestroy() {
        nativeFunctionExecutor.destroyPickup(id.value)
    }

}