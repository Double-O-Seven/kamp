package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleAngleProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, Float> {

    private val angle = ReferenceFloat()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): Float {
        nativeFunctionExecutor.getVehicleZAngle(vehicleid = thisRef.id.value, z_angle = angle)
        return angle.value
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setVehicleZAngle(vehicleid = thisRef.id.value, z_angle = value)
    }

}