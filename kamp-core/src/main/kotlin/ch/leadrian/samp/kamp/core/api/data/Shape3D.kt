package ch.leadrian.samp.kamp.core.api.data

interface Shape3D {

    operator fun contains(coordinates: Vector3D): Boolean

    val volume: Float
}