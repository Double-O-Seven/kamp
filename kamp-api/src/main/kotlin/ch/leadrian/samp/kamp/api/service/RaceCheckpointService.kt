package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.api.data.Sphere
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.RaceCheckpoint

interface RaceCheckpointService {

    fun createRaceCheckpoint(
            coordinates: Vector3D,
            size: Vector3D,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null
    ): RaceCheckpoint

    fun createRaceCheckpoint(
            sphere: Sphere,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null
    ): RaceCheckpoint

}