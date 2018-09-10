package ch.leadrian.samp.kamp.core.api.data

interface VehicleWindowStates {

    val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

    fun toVehicleWindowStates(): VehicleWindowStates

    fun toMutableVehicleWindowStates(): MutableVehicleWindowStates

}

fun vehicleWindowStatesOf(
        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
): VehicleWindowStates =
        VehicleWindowStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
