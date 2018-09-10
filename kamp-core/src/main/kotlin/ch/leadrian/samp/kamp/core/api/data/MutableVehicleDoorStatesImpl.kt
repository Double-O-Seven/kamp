package ch.leadrian.samp.kamp.core.api.data

internal data class MutableVehicleDoorStatesImpl(
        override var driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        override var passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        override var backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        override var backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
) : MutableVehicleDoorStates {

    override fun toVehicleDoorStates(): VehicleDoorStates = VehicleDoorStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )

    override fun toMutableVehicleDoorStates(): MutableVehicleDoorStates = this
}
