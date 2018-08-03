package ch.leadrian.samp.kamp.api.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class TextDrawsTest {

    @ParameterizedTest
    @CsvSource(
            "'Hi there~~~', 'Hi there???'",
            "'Hallo, wie geht es dir?', 'Hallo, wie geht es dir?'"
    )
    fun shouldSanitizeString(text: String, expectedSanitizedText: String) {
        val sanitizedText = text.sanitizeForTextDraw()

        assertThat(sanitizedText)
                .isEqualTo(expectedSanitizedText)
    }
}