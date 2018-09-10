package ch.leadrian.samp.kamp.core.api.data

import ch.leadrian.samp.kamp.core.api.util.getBit
import ch.leadrian.samp.kamp.core.api.util.setBit

data class VehicleDoorDamageStatusValue(var data: Int) {

    var isOpened: Boolean
        set(value) {
            this.data = this.data.setBit(0, if (value) 1 else 0)
        }
        get() = data.getBit(0) != 0

    var isDamaged: Boolean
        set(value) {
            this.data = this.data.setBit(1, if (value) 1 else 0)
        }
        get() = data.getBit(1) != 0

    var isRemoved: Boolean
        set(value) {
            this.data = this.data.setBit(2, if (value) 1 else 0)
        }
        get() = data.getBit(2) != 0

}