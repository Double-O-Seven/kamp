package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleDoorState(override val value: Int) : ConstantValue<Int> {
    OPEN(1),
    CLOSED(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleDoorState>(VehicleDoorState.values())
}