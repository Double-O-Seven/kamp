package ch.leadrian.samp.kamp.api.data

interface Location : Vector3D {

    val interiorId: Int

    val worldId: Int

    fun toLocation(): Location

    fun toMutableLocation(): MutableLocation

    fun distanceTo(other: Location): Float {
        if (this.interiorId != other.interiorId || this.worldId != other.worldId) {
            return Float.MAX_VALUE
        }
        return distanceTo(other as Vector3D)
    }

    fun isInRange(other: Location, distance: Float): Boolean {
        if (this.interiorId != other.interiorId || this.worldId != other.worldId) {
            return false
        }
        return isInRange(other as Vector3D, distance)
    }

}