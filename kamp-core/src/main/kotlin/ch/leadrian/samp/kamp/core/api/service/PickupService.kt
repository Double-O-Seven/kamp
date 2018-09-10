package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId

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