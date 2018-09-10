package ch.leadrian.samp.kamp.core.api.data

internal data class VehicleDoorStatesImpl(
        override val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        override val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        override val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        override val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
) : VehicleDoorStates {

    override fun toVehicleDoorStates(): VehicleDoorStates = this

    override fun toMutableVehicleDoorStates(): MutableVehicleDoorStates = MutableVehicleDoorStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )
}
