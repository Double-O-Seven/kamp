package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId

interface Pickup : Destroyable, Entity<PickupId> {

    override val id: PickupId

    val modelId: Int

    val type: Int

    val coordinates: Vector3D

    val virtualWorldId: Int?

    fun onPickUp(onPickUp: Pickup.(Player) -> Unit)

}