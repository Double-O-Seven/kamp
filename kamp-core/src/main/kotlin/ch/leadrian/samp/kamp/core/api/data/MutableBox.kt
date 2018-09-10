package ch.leadrian.samp.kamp.core.api.data

interface MutableBox : ch.leadrian.samp.kamp.core.api.data.Box {

    override var minX: Float

    override var maxX: Float

    override var minY: Float

    override var maxY: Float

    override var minZ: Float

    override var maxZ: Float
}