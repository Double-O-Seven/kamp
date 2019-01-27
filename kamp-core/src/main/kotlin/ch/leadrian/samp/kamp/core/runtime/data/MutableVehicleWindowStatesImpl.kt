package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
import ch.leadrian.samp.kamp.core.api.data.MutableVehicleWindowStates
import ch.leadrian.samp.kamp.core.api.data.VehicleWindowStates

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
