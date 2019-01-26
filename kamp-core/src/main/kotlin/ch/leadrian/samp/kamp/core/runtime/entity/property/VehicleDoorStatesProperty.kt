package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.VehicleDoorState
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorStates
import ch.leadrian.samp.kamp.core.api.data.vehicleDoorStatesOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleDoorStatesProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, VehicleDoorStates> {

    private val driver = ReferenceInt()
    private val passenger = ReferenceInt()
    private val backLeft = ReferenceInt()
    private val backRight = ReferenceInt()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): VehicleDoorStates {
        nativeFunctionExecutor.getVehicleParamsCarDoors(
                vehicleid = thisRef.id.value,
                driver = driver,
                passenger = passenger,
                backleft = backLeft,
                backright = backRight
        )
        return vehicleDoorStatesOf(
                driver = VehicleDoorState[driver.value],
                passenger = VehicleDoorState[passenger.value],
                backLeft = VehicleDoorState[backLeft.value],
                backRight = VehicleDoorState[backRight.value]
        )
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: VehicleDoorStates) {
        nativeFunctionExecutor.setVehicleParamsCarDoors(
                vehicleid = thisRef.id.value,
                driver = value.driver.value,
                passenger = value.passenger.value,
                backleft = value.backLeft.value,
                backright = value.backRight.value
        )
    }
}