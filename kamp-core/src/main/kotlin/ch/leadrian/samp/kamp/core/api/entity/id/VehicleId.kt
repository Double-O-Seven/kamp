package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class VehicleId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = VehicleId(SAMPConstants.INVALID_VEHICLE_ID)

        private val vehicleIds: Array<VehicleId> = (0 until SAMPConstants.MAX_VEHICLES).map { VehicleId(it) }.toTypedArray()

        fun valueOf(value: Int): VehicleId =
                when {
                    0 <= value && value < vehicleIds.size -> vehicleIds[value]
                    value == INVALID.value -> INVALID
                    else -> VehicleId(value)
                }
    }
}