package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleAlarmState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    IS_OR_WAS_SOUNDING(1),
    OFF(0),
    UNSET(-1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleAlarmState>(*VehicleAlarmState.values())
}