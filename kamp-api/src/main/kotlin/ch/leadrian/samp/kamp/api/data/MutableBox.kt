package ch.leadrian.samp.kamp.api.data

interface MutableBox : Box, MutableRectangle {

    override var minZ: Float

    override var maxZ: Float
}