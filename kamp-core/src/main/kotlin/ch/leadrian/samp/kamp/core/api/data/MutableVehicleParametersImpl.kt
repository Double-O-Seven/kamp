package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState

internal data class MutableVehicleParametersImpl(
        override var engine: VehicleEngineState,
        override var lights: VehicleLightsState,
        override var alarm: VehicleAlarmState,
        override var doorLock: VehicleDoorLockState,
        override var bonnet: VehicleBonnetState,
        override var boot: VehicleBootState,
        override var objective: VehicleObjectiveState
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