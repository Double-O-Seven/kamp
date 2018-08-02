package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleWindowState

internal data class MutableVehicleWindowStatesImpl(
        override var driver: VehicleWindowState,
        override var passenger: VehicleWindowState,
        override var backLeft: VehicleWindowState,
        override var backRight: VehicleWindowState
) : MutableVehicleWindowStates {

    override fun toVehicleWindowStates(): VehicleWindowStates = VehicleWindowStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )

    override fun toMutableVehicleWindowStates(): MutableVehicleWindowStates = this
}
