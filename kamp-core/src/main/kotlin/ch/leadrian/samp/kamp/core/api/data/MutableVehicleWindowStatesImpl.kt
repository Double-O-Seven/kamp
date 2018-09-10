package ch.leadrian.samp.kamp.core.api.data

internal data class MutableVehicleWindowStatesImpl(
        override var driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        override var passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        override var backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        override var backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
) : MutableVehicleWindowStates {

    override fun toVehicleWindowStates(): VehicleWindowStates = VehicleWindowStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )

    override fun toMutableVehicleWindowStates(): MutableVehicleWindowStates = this
}
