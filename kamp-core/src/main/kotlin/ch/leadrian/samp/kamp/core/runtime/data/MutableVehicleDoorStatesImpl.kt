package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
import ch.leadrian.samp.kamp.core.api.data.MutableVehicleDoorStates
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorStates

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
