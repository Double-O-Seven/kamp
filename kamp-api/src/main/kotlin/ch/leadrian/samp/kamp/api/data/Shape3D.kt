package ch.leadrian.samp.kamp.api.data

interface Shape3D : Shape2D {

    fun contains(coordinates: Vector3D): Boolean

    val volume: Float
}