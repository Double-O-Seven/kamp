package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D

interface RaceCheckpoint : Destroyable {

    var coordinates: Vector3D

    var size: Float

    var nextCoordinates: Vector3D?

    var type: ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType

    fun onEnter(onEnter: RaceCheckpoint.(Player) -> Unit)

    fun onLeave(onLeave: RaceCheckpoint.(Player) -> Unit)

}