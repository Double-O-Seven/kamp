package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
import ch.leadrian.samp.kamp.core.runtime.entity.factory.RaceCheckpointFactory
import javax.inject.Inject

class RaceCheckpointService
@Inject
internal constructor(private val raceCheckpointFactory: RaceCheckpointFactory) {

    fun createRaceCheckpoint(
            coordinates: Vector3D,
            size: Float,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null
    ): RaceCheckpoint = raceCheckpointFactory.create(
            coordinates = coordinates,
            size = size,
            type = type,
            nextCoordinates = nextCoordinates
    )

    fun createRaceCheckpoint(
            sphere: Sphere,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null
    ): RaceCheckpoint = raceCheckpointFactory.create(
            coordinates = vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z),
            size = sphere.radius,
            type = type,
            nextCoordinates = nextCoordinates
    )

}