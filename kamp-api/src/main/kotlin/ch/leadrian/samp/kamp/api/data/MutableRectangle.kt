package ch.leadrian.samp.kamp.api.data

interface MutableRectangle : Rectangle {

    override var minX: Float

    override var maxX: Float

    override var minY: Float

    override var maxY: Float
}