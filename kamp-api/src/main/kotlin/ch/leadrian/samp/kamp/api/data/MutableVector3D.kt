package ch.leadrian.samp.kamp.api.data

interface MutableVector3D : MutableVector2D, Vector3D {

    override var z: Float

    override fun plus(other: Vector2D): MutableVector3D

    override fun plus(other: Vector3D): MutableVector3D

    operator fun plusAssign(other: Vector3D) {
        this.x += other.x
        this.y += other.y
        this.z += other.z
    }

    override fun minus(other: Vector2D): MutableVector3D

    override fun minus(other: Vector3D): MutableVector3D

    operator fun minusAssign(other: Vector3D) {
        this.x -= other.x
        this.y -= other.y
        this.z -= other.z
    }

    override fun times(value: Float): MutableVector3D

    override fun timesAssign(value: Float) {
        this.x *= value
        this.y *= value
        this.z *= value
    }
}