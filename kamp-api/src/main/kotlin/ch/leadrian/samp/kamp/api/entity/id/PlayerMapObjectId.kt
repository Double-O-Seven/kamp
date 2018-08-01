package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class PlayerMapObjectId internal constructor(val value: Int) {

    companion object {

        val INVALID = PlayerMapObjectId(SAMPConstants.INVALID_OBJECT_ID)

        private val playerMapObjectId: Array<PlayerMapObjectId> = (0..SAMPConstants.MAX_OBJECTS).map { PlayerMapObjectId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerMapObjectId =
                when {
                    value == SAMPConstants.INVALID_OBJECT_ID -> INVALID
                    0 <= value && value < playerMapObjectId.size -> playerMapObjectId[value]
                    else -> PlayerMapObjectId(value)
                }
    }
}