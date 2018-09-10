package ch.leadrian.samp.kamp.core.api.data

interface MutableCircle : ch.leadrian.samp.kamp.core.api.data.Circle, Shape2D {

    override var x: Float

    override var y: Float

    override var radius: Float

}