package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class PlayerMapObjectId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = PlayerMapObjectId(SAMPConstants.INVALID_OBJECT_ID)

        private val playerMapObjectId: Array<PlayerMapObjectId> = (0 until SAMPConstants.MAX_OBJECTS).map { PlayerMapObjectId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerMapObjectId =
                when {
                    0 <= value && value < playerMapObjectId.size -> playerMapObjectId[value]
                    value == INVALID.value -> INVALID
                    else -> PlayerMapObjectId(value)
                }
    }
}