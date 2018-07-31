package ch.leadrian.samp.kamp.api.data

interface MutableCircle : Circle, Shape2D {

    override var x: Float

    override var y: Float

    override var radius: Float

}