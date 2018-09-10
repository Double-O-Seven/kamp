package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleBootState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    OPEN(1),
    CLOSED(0),
    UNSET(-1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleBootState>(*VehicleBootState.values())
}