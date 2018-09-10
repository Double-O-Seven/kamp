package ch.leadrian.samp.kamp.core.api.data

interface VehicleParameters {

    val engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState

    val lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState

    val alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState

    val doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState

    val bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState

    val boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState

    val objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState

    fun toVehicleParameters(): VehicleParameters

    fun toMutableVehicleParameters(): MutableVehicleParameters

}

fun vehicleParametersOf(
        engine: ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState,
        lights: ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState,
        alarm: ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState,
        doorLock: ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState,
        bonnet: ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState,
        boot: ch.leadrian.samp.kamp.core.api.constants.VehicleBootState,
        objective: ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
): VehicleParameters = VehicleParametersImpl(
        engine = engine,
        lights = lights,
        alarm = alarm,
        doorLock = doorLock,
        bonnet = bonnet,
        boot = boot,
        objective = objective
)
