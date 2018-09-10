package ch.leadrian.samp.kamp.core.api.data

interface VehicleDoorStates {

    val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    fun toVehicleDoorStates(): VehicleDoorStates

    fun toMutableVehicleDoorStates(): MutableVehicleDoorStates

}

fun vehicleDoorStatesOf(
        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
): VehicleDoorStates =
        VehicleDoorStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
