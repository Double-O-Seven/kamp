package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.Position
import ch.leadrian.samp.kamp.core.api.data.positionOf
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceFloat
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehiclePositionProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, Position> {

    private val x = ReferenceFloat()
    private val y = ReferenceFloat()
    private val z = ReferenceFloat()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): Position {
        nativeFunctionExecutor.getVehiclePos(vehicleid = thisRef.id.value, x = x, y = y, z = z)
        return positionOf(x = x.value, y = y.value, z = z.value, angle = thisRef.angle)
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: Position) {
        thisRef.apply {
            coordinates = value
            angle = value.angle
        }
    }

}