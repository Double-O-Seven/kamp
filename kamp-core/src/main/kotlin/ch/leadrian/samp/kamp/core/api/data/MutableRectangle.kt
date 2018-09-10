package ch.leadrian.samp.kamp.core.api.data

interface MutableRectangle : Rectangle {

    override var minX: Float

    override var maxX: Float

    override var minY: Float

    override var maxY: Float
}