package ch.leadrian.samp.kamp.core.api.entity.id

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class TextLabelIdTest {

    @ParameterizedTest
    @ValueSource(
            ints = [
                -1,
                0,
                SAMPConstants.MAX_3DTEXT_GLOBAL - 1,
                SAMPConstants.MAX_3DTEXT_GLOBAL,
                Int.MAX_VALUE,
                SAMPConstants.INVALID_3DTEXT_ID
            ]
    )
    fun shouldReturnTextLabelId(value: Int) {
        val textLabelId = TextLabelId.valueOf(value)

        assertThat(textLabelId.value)
                .isEqualTo(value)
    }

}