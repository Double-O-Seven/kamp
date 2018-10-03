package ch.leadrian.samp.kamp.core.api.text

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class TextFormatterTest {

    @Test
    fun shouldFormatText() {
        val locale = Locale.GERMANY
        val textKey1 = TextKey("test.key.abc")
        val textKey2 = TextKey("test.key.def")
        val hasTextKey = mockk<HasTextKey> {
            every { this@mockk.textKey } returns textKey1
        }
        val translatable = translateForText { "Hi there" }
        val textProvider = mockk<TextProvider> {
            every { getText(locale, textKey1) } returns "Hallo"
            every { getText(locale, textKey2) } returns "Bonjour"
        }
        val textFormatter = TextFormatter(textProvider)

        val formattedText = textFormatter.format(
                locale,
                "A: {0}, B: {1}, C: {2}, D: {3}",
                1337,
                hasTextKey,
                textKey2,
                translatable
        )

        assertThat(formattedText)
                .isEqualTo("A: 1.337, B: Hallo, C: Bonjour, D: Hi there")
    }

    @Test
    fun givenFormattingThrowsExceptionItShouldReturnUnformattedMessage() {
        val locale = Locale.GERMANY
        val messageFormatter = TextFormatter(mockk())

        val formattedMessage = messageFormatter.format(
                locale,
                "A: {0}, B: {1}",
                "Hi",
                translateForText { throw RuntimeException("fail") }
        )

        assertThat(formattedMessage)
                .isEqualTo("A: {0}, B: {1}")
    }
}