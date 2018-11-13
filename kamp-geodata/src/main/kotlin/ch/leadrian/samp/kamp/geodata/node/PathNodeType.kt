package ch.leadrian.samp.kamp.geodata.node

enum class PathNodeType {
    VEHICLE_TRAFFIC,
    BOATS,
    OTHER,
    MISSION;

    internal companion object {

        fun fromFloodFill(value: Int): PathNodeType {
            return when (value) {
                0 -> OTHER
                1 -> VEHICLE_TRAFFIC
                2 -> BOATS
                else -> MISSION
            }
        }
    }

}