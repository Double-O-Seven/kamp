package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class GangZoneId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = GangZoneId(SAMPConstants.INVALID_GANG_ZONE)

        private val gangZoneIds: Array<GangZoneId> = (0 until SAMPConstants.MAX_GANG_ZONES).map { GangZoneId(it) }.toTypedArray()

        fun valueOf(value: Int): GangZoneId =
                when {
                    0 <= value && value < gangZoneIds.size -> gangZoneIds[value]
                    value == INVALID.value -> INVALID
                    else -> GangZoneId(value)
                }
    }
}