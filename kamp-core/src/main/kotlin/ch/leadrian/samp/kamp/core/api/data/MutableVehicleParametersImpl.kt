package ch.leadrian.samp.kamp.core.api.data

internal data class MutableVehicleParametersImpl(
        override var engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
        override var lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
        override var alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
        override var doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
        override var bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
        override var boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
        override var objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
) : MutableVehicleParameters {

    override fun toVehicleParameters(): VehicleParameters = VehicleParametersImpl(
            engine = engine,
            lights = lights,
            alarm = alarm,
            doorLock = doorLock,
            bonnet = bonnet,
            boot = boot,
            objective = objective
    )

    override fun toMutableVehicleParameters(): MutableVehicleParameters = this

}