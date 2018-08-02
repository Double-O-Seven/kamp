package ch.leadrian.samp.kamp.api.constants

enum class VehicleLightsState(override val value: Int) : ConstantValue<Int> {
    ON(1),
    OFF(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleLightsState>(*VehicleLightsState.values())
}