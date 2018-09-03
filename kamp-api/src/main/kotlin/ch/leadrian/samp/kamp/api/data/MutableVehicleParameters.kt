package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.api.constants.VehicleObjectiveState

interface MutableVehicleParameters : VehicleParameters {

    override var engine: VehicleEngineState

    override var lights: VehicleLightsState

    override var alarm: VehicleAlarmState

    override var doorLock: VehicleDoorLockState

    override var bonnet: VehicleBonnetState

    override var boot: VehicleBootState

    override var objective: VehicleObjectiveState

}

fun mutableVehicleParametersOf(
        engine: VehicleEngineState,
        lights: VehicleLightsState,
        alarm: VehicleAlarmState,
        doorLock: VehicleDoorLockState,
        bonnet: VehicleBonnetState,
        boot: VehicleBootState,
        objective: VehicleObjectiveState
): MutableVehicleParameters = MutableVehicleParametersImpl(
        engine = engine,
        lights = lights,
        alarm = alarm,
        doorLock = doorLock,
        bonnet = bonnet,
        boot = boot,
        objective = objective
)
