package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class PlayerTextDrawId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = PlayerTextDrawId(SAMPConstants.INVALID_TEXT_DRAW)

        private val playerTextDrawIds: Array<PlayerTextDrawId> = (0 until SAMPConstants.MAX_PLAYER_TEXT_DRAWS)
                .map { PlayerTextDrawId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerTextDrawId =
                when {
                    0 <= value && value < playerTextDrawIds.size -> playerTextDrawIds[value]
                    value == INVALID.value -> INVALID
                    else -> PlayerTextDrawId(value)
                }
    }
}