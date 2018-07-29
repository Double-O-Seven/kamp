package ch.leadrian.samp.kamp.api.data

interface MutableLocation : Location, MutableVector3D {

    override var interiorId: Int

    override var worldId: Int

}