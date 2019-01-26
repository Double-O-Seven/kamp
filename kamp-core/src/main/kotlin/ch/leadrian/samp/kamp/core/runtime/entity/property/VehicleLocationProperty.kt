package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Location
import ch.leadrian.samp.kamp.core.api.data.locationOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleLocationProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, Location> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): Location {
        nativeFunctionExecutor.getVehiclePos(vehicleid = thisRef.id.value, x = x, y = y, z = z)
        return locationOf(
                x = x.value,
                y = y.value,
                z = z.value,
                interiorId = thisRef.interiorId,
                worldId = thisRef.virtualWorldId
        )
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: Location) {
        thisRef.apply {
            coordinates = value
            interiorId = value.interiorId
            virtualWorldId = value.virtualWorldId
        }
    }

}