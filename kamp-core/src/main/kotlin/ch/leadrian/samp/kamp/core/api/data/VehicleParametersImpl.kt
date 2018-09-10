package ch.leadrian.samp.kamp.core.api.data

internal data class VehicleParametersImpl(
        override val engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
        override val lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
        override val alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
        override val doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
        override val bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
        override val boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
        override val objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
) : VehicleParameters {

    override fun toVehicleParameters(): VehicleParameters = this

    override fun toMutableVehicleParameters(): MutableVehicleParameters = MutableVehicleParametersImpl(
            engine = engine,
            lights = lights,
            alarm = alarm,
            doorLock = doorLock,
            bonnet = bonnet,
            boot = boot,
            objective = objective
    )

}