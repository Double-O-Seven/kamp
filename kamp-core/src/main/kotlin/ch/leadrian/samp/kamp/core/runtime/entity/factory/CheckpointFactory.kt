package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import javax.inject.Inject

internal class CheckpointFactory
@Inject
constructor(private val playerRegistry: PlayerRegistry) {

    fun create(coordinates: Vector3D, size: Float): Checkpoint =
            Checkpoint(
                    coordinates = coordinates,
                    size = size,
                    playerRegistry = playerRegistry
            )

}