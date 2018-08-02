package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleDoorState

interface MutableVehicleDoorStates : VehicleDoorStates {

    override var driver: VehicleDoorState

    override var passenger: VehicleDoorState

    override var backLeft: VehicleDoorState

    override var backRight: VehicleDoorState

}

fun mutableVehicleDoorStatesOf(
        driver: VehicleDoorState,
        passenger: VehicleDoorState,
        backLeft: VehicleDoorState,
        backRight: VehicleDoorState
): MutableVehicleDoorStates =
        MutableVehicleDoorStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )