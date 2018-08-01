package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class PlayerId internal constructor(val value: Int) {

    companion object {

        val INVALID = PlayerId(SAMPConstants.INVALID_PLAYER_ID)

        private val playerIds: Array<PlayerId> = (0..SAMPConstants.MAX_PLAYERS).map { PlayerId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerId =
                when {
                    value == SAMPConstants.INVALID_PLAYER_ID -> INVALID
                    0 <= value && value < playerIds.size -> playerIds[value]
                    else -> PlayerId(value)
                }
    }
}