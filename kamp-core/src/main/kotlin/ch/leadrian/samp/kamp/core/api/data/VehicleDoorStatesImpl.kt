package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

internal data class VehicleDoorStatesImpl(
        override val driver: VehicleDoorState,
        override val passenger: VehicleDoorState,
        override val backLeft: VehicleDoorState,
        override val backRight: VehicleDoorState
) : VehicleDoorStates {

    override fun toVehicleDoorStates(): VehicleDoorStates = this

    override fun toMutableVehicleDoorStates(): MutableVehicleDoorStates = MutableVehicleDoorStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )
}
