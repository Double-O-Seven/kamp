package ch.leadrian.samp.kamp.core.api.data

interface MutableVehicleWindowStates : VehicleWindowStates {

    override var driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    override var passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    override var backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    override var backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

}

fun mutableVehicleWindowStatesOf(
        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
): MutableVehicleWindowStates =
        MutableVehicleWindowStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
