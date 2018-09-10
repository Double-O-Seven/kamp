package ch.leadrian.samp.kamp.core.api.constants

enum class RaceCheckpointType(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    NORMAL(0),
    FINISH(1),
    NOTHING(2),
    AIR_NORMAL(3),
    AIR_FINISH(4),
    AIR_ROTATES_AND_STOPS(5),
    AIR_INCREASES_DECREASE_AND_DISAPPEARS(6),
    AIR_SWINGS_DOWN_AND_UP(7),
    AIR_SWINGS_UP_AND_DOWN(8);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, RaceCheckpointType>(*RaceCheckpointType.values())
}