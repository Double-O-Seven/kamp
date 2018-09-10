package ch.leadrian.samp.kamp.core.api.constants

enum class VehicleDoorLockState(override val value: Int) : ch.leadrian.samp.kamp.core.api.constants.ConstantValue<Int> {
    LOCKED(1),
    UNLOCKED(0),
    UNSET(-1);

    companion object : ch.leadrian.samp.kamp.core.api.constants.ConstantValueRegistry<Int, VehicleDoorLockState>(*VehicleDoorLockState.values())
}