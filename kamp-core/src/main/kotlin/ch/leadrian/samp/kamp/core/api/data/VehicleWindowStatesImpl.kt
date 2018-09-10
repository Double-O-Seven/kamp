package ch.leadrian.samp.kamp.core.api.data

internal data class VehicleWindowStatesImpl(
        override val driver: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        override val passenger: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        override val backLeft: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState,
        override val backRight: ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
) : VehicleWindowStates {

    override fun toVehicleWindowStates(): VehicleWindowStates = this

    override fun toMutableVehicleWindowStates(): MutableVehicleWindowStates = MutableVehicleWindowStatesImpl(
            driver = driver,
            passenger = passenger,
            backLeft = backLeft,
            backRight = backRight
    )
}
