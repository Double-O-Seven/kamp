package ch.leadrian.samp.kamp.api.constants

enum class VehicleWindowState(override val value: Int) : ConstantValue<Int> {
    OPEN(0),
    CLOSED(1),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleWindowState>(*VehicleWindowState.values())
}