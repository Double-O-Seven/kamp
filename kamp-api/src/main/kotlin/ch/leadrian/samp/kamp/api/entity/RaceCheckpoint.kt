package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.api.data.Vector3D

interface RaceCheckpoint : Destroyable {

    var coordinates: Vector3D

    var size: Float

    var nextCoordinates: Vector3D?

    var type: RaceCheckpointType

    fun onEnter(onEnter: RaceCheckpoint.(Player) -> Unit)

    fun onLeave(onLeave: RaceCheckpoint.(Player) -> Unit)

}