package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.constants.VehicleAlarmState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBonnetState
import ch.leadrian.samp.kamp.core.api.constants.VehicleBootState
import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorLockState
import ch.leadrian.samp.kamp.core.api.constants.VehicleEngineState
import ch.leadrian.samp.kamp.core.api.constants.VehicleLightsState
import ch.leadrian.samp.kamp.core.api.constants.VehicleObjectiveState
import ch.leadrian.samp.kamp.core.api.data.VehicleParameters
import ch.leadrian.samp.kamp.core.api.data.vehicleParametersOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleParametersDelegate(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, VehicleParameters> {

    private val engine = ReferenceInt()
    private val objective = ReferenceInt()
    private val boot = ReferenceInt()
    private val bonnet = ReferenceInt()
    private val lights = ReferenceInt()
    private val alarm = ReferenceInt()
    private val doors = ReferenceInt()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): VehicleParameters {
        nativeFunctionExecutor.getVehicleParamsEx(
                vehicleid = thisRef.id.value,
                engine = engine,
                objective = objective,
                boot = boot,
                bonnet = bonnet,
                lights = lights,
                alarm = alarm,
                doors = doors
        )
        return vehicleParametersOf(
                engine = VehicleEngineState[engine.value],
                objective = VehicleObjectiveState[objective.value],
                boot = VehicleBootState[boot.value],
                bonnet = VehicleBonnetState[bonnet.value],
                lights = VehicleLightsState[lights.value],
                alarm = VehicleAlarmState[alarm.value],
                doorLock = VehicleDoorLockState[doors.value]
        )
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: VehicleParameters) {
        nativeFunctionExecutor.setVehicleParamsEx(
                vehicleid = thisRef.id.value,
                engine = value.engine.value,
                objective = value.objective.value,
                boot = value.boot.value,
                bonnet = value.bonnet.value,
                lights = value.lights.value,
                alarm = value.alarm.value,
                doors = value.doorLock.value
        )
    }

}