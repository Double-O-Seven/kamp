package ch.leadrian.samp.kamp.api.data

import ch.leadrian.samp.kamp.api.util.getByte
import ch.leadrian.samp.kamp.api.util.setByte

data class VehiclePanelDamageStatus(var value: Int) {

    var frontLeftPanel: Int
        set(value) {
            this.value = this.value.setByte(0, value)
        }
        get() = value.getByte(0)

    var frontRightPanel: Int
        set(value) {
            this.value = this.value.setByte(1, value)
        }
        get() = value.getByte(1)

    var rearLeftPanel: Int
        set(value) {
            this.value = this.value.setByte(2, value)
        }
        get() = value.getByte(2)

    var rearRightPanel: Int
        set(value) {
            this.value = this.value.setByte(3, value)
        }
        get() = value.getByte(3)

    var windshield: Int
        set(value) {
            this.value = this.value.setByte(4, value)
        }
        get() = value.getByte(4)

    var frontBumper: Int
        set(value) {
            this.value = this.value.setByte(5, value)
        }
        get() = value.getByte(5)

    var rearBumper: Int
        set(value) {
            this.value = this.value.setByte(6, value)
        }
        get() = value.getByte(6)

}


