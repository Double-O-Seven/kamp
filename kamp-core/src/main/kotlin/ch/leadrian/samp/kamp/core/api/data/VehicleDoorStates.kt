package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

interface VehicleDoorStates {

    val driver: VehicleDoorState

    val passenger: VehicleDoorState

    val backLeft: VehicleDoorState

    val backRight: VehicleDoorState

    fun toVehicleDoorStates(): VehicleDoorStates

    fun toMutableVehicleDoorStates(): MutableVehicleDoorStates

}

fun vehicleDoorStatesOf(
        driver: VehicleDoorState,
        passenger: VehicleDoorState,
        backLeft: VehicleDoorState,
        backRight: VehicleDoorState
): VehicleDoorStates =
        VehicleDoorStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
