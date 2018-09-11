package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.RaceCheckpointType
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.RaceCheckpoint
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import javax.inject.Inject

internal class RaceCheckpointFactory
@Inject
constructor(private val playerRegistry: PlayerRegistry) {

    fun create(
            coordinates: Vector3D,
            size: Float,
            type: RaceCheckpointType,
            nextCoordinates: Vector3D? = null
    ): RaceCheckpoint = RaceCheckpoint(
            coordinates = coordinates,
            size = size,
            type = type,
            nextCoordinates = nextCoordinates,
            playerRegistry = playerRegistry
    )

}