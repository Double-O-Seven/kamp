package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

interface MutableVehicleWindowStates : VehicleWindowStates {

    override var driver: VehicleWindowState

    override var passenger: VehicleWindowState

    override var backLeft: VehicleWindowState

    override var backRight: VehicleWindowState

}

fun mutableVehicleWindowStatesOf(
        driver: VehicleWindowState,
        passenger: VehicleWindowState,
        backLeft: VehicleWindowState,
        backRight: VehicleWindowState
): MutableVehicleWindowStates =
        MutableVehicleWindowStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
