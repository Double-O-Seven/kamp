package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
import ch.leadrian.samp.kamp.core.runtime.data.VehicleParametersImpl

interface VehicleParameters {

    val engine: VehicleEngineState

    val lights: VehicleLightsState

    val alarm: VehicleAlarmState

    val doorLock: VehicleDoorLockState

    val bonnet: VehicleBonnetState

    val boot: VehicleBootState

    val objective: VehicleObjectiveState

    fun toVehicleParameters(): VehicleParameters

    fun toMutableVehicleParameters(): MutableVehicleParameters

}

fun vehicleParametersOf(
        engine: VehicleEngineState,
        lights: VehicleLightsState,
        alarm: VehicleAlarmState,
        doorLock: VehicleDoorLockState,
        bonnet: VehicleBonnetState,
        boot: VehicleBootState,
        objective: VehicleObjectiveState
): VehicleParameters = VehicleParametersImpl(
        engine = engine,
        lights = lights,
        alarm = alarm,
        doorLock = doorLock,
        bonnet = bonnet,
        boot = boot,
        objective = objective
)
