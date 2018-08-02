package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.util.getByte
import ch.leadrian.samp.kamp.api.util.setByte

data class VehicleDoorsDamageStatus(var value: Int) {

    var hood: VehicleDoorDamageStatusValue
        set(value) {
            this.value = this.value.setByte(0, value.data)
        }
        get() = VehicleDoorDamageStatusValue(value.getByte(0))

    var trunk: VehicleDoorDamageStatusValue
        set(value) {
            this.value = this.value.setByte(1, value.data)
        }
        get() = VehicleDoorDamageStatusValue(value.getByte(1))

    var driver: VehicleDoorDamageStatusValue
        set(value) {
            this.value = this.value.setByte(2, value.data)
        }
        get() = VehicleDoorDamageStatusValue(value.getByte(2))

    var coDriver: VehicleDoorDamageStatusValue
        set(value) {
            this.value = this.value.setByte(3, value.data)
        }
        get() = VehicleDoorDamageStatusValue(value.getByte(3))

}