package ch.leadrian.samp.kamp.api.data

interface Shape3D {

    fun contains(coordinates: Vector3D): Boolean

    val volume: Float
}