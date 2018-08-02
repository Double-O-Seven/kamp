package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class TextDrawId internal constructor(val value: Int) {

    companion object {

        val INVALID = TextDrawId(SAMPConstants.INVALID_TEXT_DRAW)

        private val textDrawIds: Array<TextDrawId> = (0..SAMPConstants.MAX_TEXT_DRAWS).map { TextDrawId(it) }.toTypedArray()

        fun valueOf(value: Int): TextDrawId =
                when {
                    value == SAMPConstants.INVALID_TEXT_DRAW -> INVALID
                    0 <= value && value < textDrawIds.size -> textDrawIds[value]
                    else -> TextDrawId(value)
                }
    }
}