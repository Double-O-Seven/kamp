package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Pickup
import ch.leadrian.samp.kamp.api.entity.id.PickupId

interface PickupService {

    fun createPickup(
            modelId: Int,
            type: Int,
            coordinates: Vector3D,
            virtualWorldId: Int? = null
    ): Pickup

    fun exists(pickupId: PickupId): Boolean

    fun getPickup(pickupId: PickupId): Pickup

    fun getAllPickups(): List<Pickup>

}