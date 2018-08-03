package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class GangZoneId internal constructor(val value: Int) {

    companion object {

        val INVALID = GangZoneId(SAMPConstants.INVALID_GANG_ZONE)

        private val gangZoneIds: Array<GangZoneId> = (0..SAMPConstants.MAX_GANG_ZONES).map { GangZoneId(it) }.toTypedArray()

        fun valueOf(value: Int): GangZoneId =
                when {
                    value == SAMPConstants.INVALID_GANG_ZONE -> INVALID
                    0 <= value && value < gangZoneIds.size -> gangZoneIds[value]
                    else -> GangZoneId(value)
                }
    }
}