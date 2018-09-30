package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint
import ch.leadrian.samp.kamp.core.runtime.entity.factory.CheckpointFactory
import javax.inject.Inject

class CheckpointService
@Inject
internal constructor(
        private val checkpointFactory: CheckpointFactory
) {

    fun createCheckpoint(coordinates: Vector3D, size: Float): Checkpoint =
            checkpointFactory.create(coordinates, size)

    fun createCheckpoint(sphere: Sphere): Checkpoint =
            checkpointFactory.create(vector3DOf(x = sphere.x, y = sphere.y, z = sphere.z), sphere.radius)
}