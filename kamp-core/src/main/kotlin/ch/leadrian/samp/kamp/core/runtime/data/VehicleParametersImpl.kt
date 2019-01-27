package ch.leadrian.samp.kamp.core.runtime.data

import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
import ch.leadrian.samp.kamp.core.api.data.MutableVehicleParameters
import ch.leadrian.samp.kamp.core.api.data.VehicleParameters

internal data class VehicleParametersImpl(
        override val engine: VehicleEngineState,
        override val lights: VehicleLightsState,
        override val alarm: VehicleAlarmState,
        override val doorLock: VehicleDoorLockState,
        override val bonnet: VehicleBonnetState,
        override val boot: VehicleBootState,
        override val objective: VehicleObjectiveState
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