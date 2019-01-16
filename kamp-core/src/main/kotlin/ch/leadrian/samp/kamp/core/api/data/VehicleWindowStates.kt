package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

interface VehicleWindowStates {

    val driver: VehicleWindowState

    val passenger: VehicleWindowState

    val backLeft: VehicleWindowState

    val backRight: VehicleWindowState

    fun toVehicleWindowStates(): VehicleWindowStates

    fun toMutableVehicleWindowStates(): MutableVehicleWindowStates

}

fun vehicleWindowStatesOf(
        driver: VehicleWindowState,
        passenger: VehicleWindowState,
        backLeft: VehicleWindowState,
        backRight: VehicleWindowState
): VehicleWindowStates =
        VehicleWindowStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
