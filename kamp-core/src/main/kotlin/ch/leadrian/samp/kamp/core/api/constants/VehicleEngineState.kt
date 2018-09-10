package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleEngineState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    RUNNING(1),
    OFF(0),
    UNSET(-1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleEngineState>(*VehicleEngineState.values())
}