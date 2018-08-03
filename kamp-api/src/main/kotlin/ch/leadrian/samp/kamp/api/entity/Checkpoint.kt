package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Vector3D

interface Checkpoint : Destroyable {

    val coordinates: Vector3D

    val size: Float

    fun show(forPlayer: Player)

    fun hide(forPlayer: Player)

    fun onEnter(onEnter: Checkpoint.(Player) -> Unit)

    fun onLeave(onEnter: Checkpoint.(Player) -> Unit)

}