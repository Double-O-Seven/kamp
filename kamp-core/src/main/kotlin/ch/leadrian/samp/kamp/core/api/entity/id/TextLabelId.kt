package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants

data class TextLabelId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = TextLabelId(SAMPConstants.INVALID_3DTEXT_ID)

        private val textLabelIds: Array<TextLabelId> = (0 until SAMPConstants.MAX_3DTEXT_GLOBAL).map { TextLabelId(it) }.toTypedArray()

        fun valueOf(value: Int): TextLabelId =
                when {
                    0 <= value && value < textLabelIds.size -> textLabelIds[value]
                    value == INVALID.value -> INVALID
                    else -> TextLabelId(value)
                }
    }
}