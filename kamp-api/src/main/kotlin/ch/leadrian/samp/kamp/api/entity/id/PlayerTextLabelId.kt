package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class PlayerTextLabelId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = PlayerTextLabelId(SAMPConstants.INVALID_3DTEXT_ID)

        private val playerTextLabelIds: Array<PlayerTextLabelId> = (0 until SAMPConstants.MAX_3DTEXT_PLAYER).map { PlayerTextLabelId(it) }.toTypedArray()

        fun valueOf(value: Int): PlayerTextLabelId =
                when {
                    0 <= value && value < playerTextLabelIds.size -> playerTextLabelIds[value]
                    value == INVALID.value -> INVALID
                    else -> PlayerTextLabelId(value)
                }
    }
}