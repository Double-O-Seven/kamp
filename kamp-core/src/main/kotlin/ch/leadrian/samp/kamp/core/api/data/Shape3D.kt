package ch.leadrian.samp.kamp.core.api.data

interface Shape3D {

    fun contains(coordinates: Vector3D): Boolean

    val volume: Float
}