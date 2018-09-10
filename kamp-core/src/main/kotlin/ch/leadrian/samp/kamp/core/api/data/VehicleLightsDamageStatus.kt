package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.util.getBit
import ch.leadrian.samp.kamp.core.api.util.setBit

data class VehicleLightsDamageStatus(var value: Int) {

    var isFrontLeftLightDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(0, if (value) 1 else 0)
        }
        get() = this.value.getBit(0) != 0

    var isFrontRightLightDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(2, if (value) 1 else 0)
        }
        get() = this.value.getBit(2) != 0

    var areBackLightsDamaged: Boolean
        set(value) {
            this.value = this.value.setBit(6, if (value) 1 else 0)
        }
        get() = this.value.getBit(6) != 0

}