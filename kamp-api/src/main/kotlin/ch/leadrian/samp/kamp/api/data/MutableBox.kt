package ch.leadrian.samp.kamp.api.data

interface MutableBox : Box {

    override var minX: Float

    override var maxX: Float

    override var minY: Float

    override var maxY: Float

    override var minZ: Float

    override var maxZ: Float
}