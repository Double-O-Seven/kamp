package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class PlayerId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = PlayerId(SAMPConstants.INVALID_PLAYER_ID)

        private val playerIds: Array<PlayerId> = (0 until SAMPConstants.MAX_PLAYERS).map { PlayerId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerId =
                when {
                    0 <= value && value < playerIds.size -> playerIds[value]
                    value == INVALID.value -> INVALID
                    else -> PlayerId(value)
                }
    }
}