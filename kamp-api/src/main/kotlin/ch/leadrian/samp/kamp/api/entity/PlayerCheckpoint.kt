package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Vector3D

interface PlayerCheckpoint : Destroyable {

    val coordinates: Vector3D

    val size: Float
}