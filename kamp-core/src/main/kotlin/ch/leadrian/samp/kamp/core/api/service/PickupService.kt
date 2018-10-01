package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PickupFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import javax.inject.Inject

class PickupService
@Inject
internal constructor(
        private val pickupFactory: PickupFactory,
        private val pickupRegistry: PickupRegistry
) {

    fun createPickup(
            modelId: Int,
            type: Int,
            coordinates: Vector3D,
            virtualWorldId: Int? = null
    ): Pickup = pickupFactory.create(modelId, type, coordinates, virtualWorldId)

    fun isValidPickup(pickupId: PickupId): Boolean = pickupRegistry[pickupId] != null

    fun getPickup(pickupId: PickupId): Pickup =
            pickupRegistry[pickupId] ?: throw NoSuchEntityException("No pickup with ID ${pickupId.value}")

    fun getAllPickups(): List<Pickup> = pickupRegistry.getAll()

}