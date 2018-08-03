package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.api.data.Vector3D

interface RaceCheckpoint : Destroyable {

    val coordinates: Vector3D

    val nextCoordinates: Vector3D?

    val type: RaceCheckpointType

    fun show(forPlayer: Player)

    fun hide(forPlayer: Player)
}