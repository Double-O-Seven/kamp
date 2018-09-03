package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.id.PickupId

interface Pickup : Destroyable, Entity<PickupId> {

    override val id: PickupId

    val modelId: Int

    val type: Int

    val coordinates: Vector3D

    val virtualWorldId: Int

}