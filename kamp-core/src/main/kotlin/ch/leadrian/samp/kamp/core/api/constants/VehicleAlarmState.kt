package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleAlarmState(override val value: Int) : ConstantValue<Int> {
    IS_OR_WAS_SOUNDING(1),
    OFF(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleAlarmState>(VehicleAlarmState.values())
}