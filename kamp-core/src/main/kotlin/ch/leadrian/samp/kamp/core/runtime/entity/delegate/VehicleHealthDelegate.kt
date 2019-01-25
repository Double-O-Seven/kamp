package ch.leadrian.samp.kamp.core.runtime.entity.delegate

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleHealthDelegate(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, Float> {

    private val health = ReferenceFloat()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): Float {
        nativeFunctionExecutor.getVehicleHealth(vehicleid = thisRef.id.value, health = health)
        return health.value
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: Float) {
        nativeFunctionExecutor.setVehicleHealth(vehicleid = thisRef.id.value, health = value)
    }

}