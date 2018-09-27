package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Sphere
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Checkpoint

interface CheckpointService {

    fun createCheckpoint(coordinates: Vector3D, size: Float): Checkpoint

    fun createCheckpoint(sphere: Sphere): Checkpoint

}