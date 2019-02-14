package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleObjectiveState(override val value: Int) : ConstantValue<Int> {
    ON(1),
    OFF(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleObjectiveState>(VehicleObjectiveState.values())
}