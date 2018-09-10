package ch.leadrian.samp.kamp.core.api.data

interface MutableSphere : Sphere, Shape3D {

    override var x: Float

    override var y: Float

    override var z: Float

    override var radius: Float
}