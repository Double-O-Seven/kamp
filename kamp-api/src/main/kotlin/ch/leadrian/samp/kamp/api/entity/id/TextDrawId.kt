package ch.leadrian.samp.kamp.api.entity.id

import ch.leadrian.samp.kamp.api.constants.SAMPConstants

data class TextDrawId internal constructor(override val value: Int) : EntityId {

    companion object {

        val INVALID = TextDrawId(SAMPConstants.INVALID_TEXT_DRAW)

        private val textDrawIds: Array<TextDrawId> = (0 until SAMPConstants.MAX_TEXT_DRAWS).map { TextDrawId(it) }.toTypedArray()

        fun valueOf(value: Int): TextDrawId =
                when {
                    0 <= value && value < textDrawIds.size -> textDrawIds[value]
                    value == INVALID.value -> INVALID
                    else -> TextDrawId(value)
                }
    }
}