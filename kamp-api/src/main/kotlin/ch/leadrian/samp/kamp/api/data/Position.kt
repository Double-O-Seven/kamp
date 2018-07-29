package ch.leadrian.samp.kamp.api.data

interface Position : Vector3D {

    val angle: Float

    fun toPosition(): Position

    fun toMutablePosition(): MutablePosition

}