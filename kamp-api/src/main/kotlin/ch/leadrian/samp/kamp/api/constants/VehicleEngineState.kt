package ch.leadrian.samp.kamp.api.constants

enum class VehicleEngineState(override val value: Int) : ConstantValue<Int> {
    RUNNING(1),
    OFF(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleEngineState>(*VehicleEngineState.values())
}