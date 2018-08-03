package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class TextLabelId internal constructor(val value: Int) {

    companion object {

        val INVALID = TextLabelId(SAMPConstants.INVALID_3DTEXT_ID)

        private val textLabelIds: Array<TextLabelId> = (0..SAMPConstants.MAX_3DTEXT_GLOBAL).map { TextLabelId(it) }.toTypedArray()

        fun valueOf(value: Int): TextLabelId =
                when {
                    value == SAMPConstants.INVALID_3DTEXT_ID -> INVALID
                    0 <= value && value < textLabelIds.size -> textLabelIds[value]
                    else -> TextLabelId(value)
                }
    }
}