package ch.leadrian.samp.kamp.core.api.data

interface Shape2D {

    fun contains(coordinates: Vector2D): Boolean

    val area: Float

}