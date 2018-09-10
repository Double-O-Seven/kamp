package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState

internal data class VehicleWindowStatesImpl(
        override val driver: VehicleWindowState,
        override val passenger: VehicleWindowState,
        override val backLeft: VehicleWindowState,
        override val backRight: VehicleWindowState
) : VehicleWindowStates {

    override fun toVehicleWindowStates(): VehicleWindowStates = this

    override fun toMutableVehicleWindowStates(): MutableVehicleWindowStates = MutableVehicleWindowStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )
}
