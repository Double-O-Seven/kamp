package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class TextDrawIdTest {

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_TEXT_DRAWS - 1, SAMPConstants.MAX_TEXT_DRAWS, Int.MAX_VALUE, SAMPConstants.INVALID_TEXT_DRAW])
    fun shouldReturnTextDrawId(value: Int) {
        val textDrawId = TextDrawId.valueOf(value)

        assertThat(textDrawId.value)
                .isEqualTo(value)
    }

}