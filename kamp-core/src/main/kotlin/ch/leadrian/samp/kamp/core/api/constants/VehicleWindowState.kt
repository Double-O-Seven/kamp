package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleWindowState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    OPEN(0),
    CLOSED(1),
    UNSET(-1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleWindowState>(*VehicleWindowState.values())
}