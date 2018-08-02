package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleDoorState

internal data class MutableVehicleDoorStatesImpl(
        override var driver: VehicleDoorState,
        override var passenger: VehicleDoorState,
        override var backLeft: VehicleDoorState,
        override var backRight: VehicleDoorState
) : MutableVehicleDoorStates {

    override fun toVehicleDoorStates(): VehicleDoorStates = VehicleDoorStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )

    override fun toMutableVehicleDoorStates(): MutableVehicleDoorStates = this
}
