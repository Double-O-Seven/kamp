package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleSirenState(override val value: Int) : ConstantValue<Int> {
    ON(1),
    OFF(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleSirenState>(*VehicleSirenState.values())
}