package ch.leadrian.samp.kamp.api.data

interface Location : Vector3D {

    val interiorId: Int

    val virtualWorldId: Int

    fun toLocation(): Location

    fun toMutableLocation(): MutableLocation

    fun distanceTo(other: Location): Float {
        if (this.interiorId != other.interiorId || this.virtualWorldId != other.virtualWorldId) {
            return Float.POSITIVE_INFINITY
        }
        return distanceTo(other as Vector3D)
    }

    fun isInRange(other: Location, distance: Float): Boolean {
        if (this.interiorId != other.interiorId || this.virtualWorldId != other.virtualWorldId) {
            return false
        }
        return isInRange(other as Vector3D, distance)
    }

    override fun plus(other: Vector2D): Location

    override fun plus(other: Vector3D): Location

    override fun minus(other: Vector2D): Location

    override fun minus(other: Vector3D): Location

    override fun times(value: Float): Location

    override fun div(value: Float): Location

}