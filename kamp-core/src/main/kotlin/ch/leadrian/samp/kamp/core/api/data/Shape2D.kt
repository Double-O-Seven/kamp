package ch.leadrian.samp.kamp.core.api.data

interface Shape2D {

    operator fun contains(coordinates: Vector2D): Boolean

    val area: Float

}