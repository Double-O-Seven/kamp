package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Quaternion
import ch.leadrian.samp.kamp.core.api.data.quaternionOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class VehicleRotationQuaternionProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadOnlyProperty<Vehicle, Quaternion> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()
    private val w = ReferenceFloat()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): Quaternion {
        nativeFunctionExecutor.getVehicleRotationQuat(vehicleid = thisRef.id.value, x = x, y = y, z = z, w = w)
        return quaternionOf(x = x.value, y = y.value, z = z.value, w = w.value)
    }

}