package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.util.getBit
import ch.leadrian.samp.kamp.core.api.util.setBit

data class VehicleTiresDamageStatus(var value: Int) {

    var isFrontLeftTireDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(3, if (value) 1 else 0)
        }
        get() = this.value.getBit(3) != 0

    var isBackLeftTireDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(2, if (value) 1 else 0)
        }
        get() = this.value.getBit(2) != 0

    var isFrontRightTireDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(1, if (value) 1 else 0)
        }
        get() = this.value.getBit(1) != 0

    var isBackRightTireDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(0, if (value) 1 else 0)
        }
        get() = this.value.getBit(0) != 0

}