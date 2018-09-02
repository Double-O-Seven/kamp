package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Vector3D

interface Checkpoint : Destroyable {

    var coordinates: Vector3D

    var size: Float

    fun onEnter(onEnter: Checkpoint.(Player) -> Unit)

    fun onLeave(onLeave: Checkpoint.(Player) -> Unit)

}