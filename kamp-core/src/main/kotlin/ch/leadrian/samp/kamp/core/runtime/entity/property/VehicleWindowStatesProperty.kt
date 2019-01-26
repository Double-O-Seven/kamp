package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.constants.VehicleWindowState
import ch.leadrian.samp.kamp.core.api.data.VehicleWindowStates
import ch.leadrian.samp.kamp.core.api.data.vehicleWindowStatesOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleWindowStatesProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, VehicleWindowStates> {

    private val driver = ReferenceInt()
    private val passenger = ReferenceInt()
    private val backLeft = ReferenceInt()
    private val backRight = ReferenceInt()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): VehicleWindowStates {
        nativeFunctionExecutor.getVehicleParamsCarWindows(
                vehicleid = thisRef.id.value,
                driver = driver,
                passenger = passenger,
                backleft = backLeft,
                backright = backRight
        )
        return vehicleWindowStatesOf(
                driver = VehicleWindowState[driver.value],
                passenger = VehicleWindowState[passenger.value],
                backLeft = VehicleWindowState[backLeft.value],
                backRight = VehicleWindowState[backRight.value]
        )
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: VehicleWindowStates) {
        nativeFunctionExecutor.setVehicleParamsCarWindows(
                vehicleid = thisRef.id.value,
                driver = value.driver.value,
                passenger = value.passenger.value,
                backleft = value.backLeft.value,
                backright = value.backRight.value
        )
    }
}