package ch.leadrian.samp.kamp.core.api.data

interface MutableVehicleDoorStates : VehicleDoorStates {

    override var driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    override var passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    override var backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

    override var backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState

}

fun mutableVehicleDoorStatesOf(
        driver: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState,
        backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
): MutableVehicleDoorStates =
        MutableVehicleDoorStatesImpl(
                driver = driver,
                passenger = passenger,
                backLeft = backLeft,
                backRight = backRight
        )
