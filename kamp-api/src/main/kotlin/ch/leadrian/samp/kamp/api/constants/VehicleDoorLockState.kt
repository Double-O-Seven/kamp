package ch.leadrian.samp.kamp.api.constants

enum class VehicleDoorLockState(override val value: Int) : ConstantValue<Int> {
    LOCKED(1),
    UNLOCKED(0),
    UNSET(-1);

    companion object : ConstantValueRegistry<Int, VehicleDoorLockState>(*VehicleDoorLockState.values())
}