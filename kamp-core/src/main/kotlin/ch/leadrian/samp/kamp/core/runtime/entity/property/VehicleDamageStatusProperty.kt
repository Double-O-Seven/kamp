package ch.leadrian.samp.kamp.core.runtime.entity.property

import ch.leadrian.samp.kamp.core.api.data.VehicleDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleDoorsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleLightsDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehiclePanelDamageStatus
import ch.leadrian.samp.kamp.core.api.data.VehicleTiresDamageStatus
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.types.ReferenceInt
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class VehicleDamageStatusProperty(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : ReadWriteProperty<Vehicle, VehicleDamageStatus> {

    private val lights = ReferenceInt()
    private val doors = ReferenceInt()
    private val panels = ReferenceInt()
    private val tires = ReferenceInt()

    override fun getValue(thisRef: Vehicle, property: KProperty<*>): VehicleDamageStatus {
        nativeFunctionExecutor.getVehicleDamageStatus(
                vehicleid = thisRef.id.value,
                lights = lights,
                doors = doors,
                panels = panels,
                tires = tires
        )
        return VehicleDamageStatus(
                tires = VehicleTiresDamageStatus(tires.value),
                panels = VehiclePanelDamageStatus(panels.value),
                doors = VehicleDoorsDamageStatus(doors.value),
                lights = VehicleLightsDamageStatus(lights.value)
        )
    }

    override fun setValue(thisRef: Vehicle, property: KProperty<*>, value: VehicleDamageStatus) {
        nativeFunctionExecutor.updateVehicleDamageStatus(
                vehicleid = thisRef.id.value,
                tires = value.tires.value,
                panels = value.panels.value,
                doors = value.doors.value,
                lights = value.lights.value
        )
    }
}
