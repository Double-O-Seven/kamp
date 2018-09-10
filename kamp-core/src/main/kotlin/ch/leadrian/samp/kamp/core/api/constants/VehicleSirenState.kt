package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleSirenState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    ON(1),
    OFF(0),
    UNSET(-1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleSirenState>(*VehicleSirenState.values())
}