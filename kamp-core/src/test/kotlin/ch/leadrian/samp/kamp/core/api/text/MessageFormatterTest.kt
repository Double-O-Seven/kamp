package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class MessageFormatterTest {

    @Test
    fun shouldFormatMessage() {
        val locale = Locale.GERMANY
        val color = Colors.RED
        val textKey1 = TextKey("test.key.abc")
        val textKey2 = TextKey("test.key.def")
        val messageArgument = mockk<MessageArgument> {
            every { getText(locale, color) } returns "Hi there"
        }
        val hasTextKey = mockk<HasTextKey> {
            every { this@mockk.textKey } returns textKey1
        }
        val textProvider = mockk<TextProvider> {
            every { getText(locale, textKey1) } returns "Hallo"
            every { getText(locale, textKey2) } returns "Bonjour"
        }
        val messageFormatter = MessageFormatter(textProvider)

        val formattedMessage = messageFormatter.format(
                locale,
                color,
                "A: {0}, B: {1}, C: {2}, D: {3}, E: {4}",
                1337,
                messageArgument,
                hasTextKey,
                textKey2,
                colorOf(0x11AADDFF)
        )

        assertThat(formattedMessage)
                .isEqualTo("A: 1.337, B: Hi there, C: Hallo, D: Bonjour, E: {11aadd}")
    }

    @Test
    fun shouldFormatProvidedMessage() {
        val messageTextKey = TextKey("test.msg")
        val locale = Locale.GERMANY
        val color = Colors.RED
        val textKey1 = TextKey("test.key.abc")
        val textKey2 = TextKey("test.key.def")
        val messageArgument = mockk<MessageArgument> {
            every { getText(locale, color) } returns "Hi there"
        }
        val hasTextKey = mockk<HasTextKey> {
            every { this@mockk.textKey } returns textKey1
        }
        val textProvider = mockk<TextProvider> {
            every { getText(locale, messageTextKey) } returns "A: {0}, B: {1}, C: {2}, D: {3}, E: {4}"
            every { getText(locale, textKey1) } returns "Hallo"
            every { getText(locale, textKey2) } returns "Bonjour"
        }
        val messageFormatter = MessageFormatter(textProvider)

        val formattedMessage = messageFormatter.format(
                locale,
                color,
                messageTextKey,
                1337,
                messageArgument,
                hasTextKey,
                textKey2,
                colorOf(0x11AADDFF)
        )

        assertThat(formattedMessage)
                .isEqualTo("A: 1.337, B: Hi there, C: Hallo, D: Bonjour, E: {11aadd}")
    }

    @Test
    fun givenFormattingThrowsExceptionItShouldReturnUnformattedMessage() {
        val locale = Locale.GERMANY
        val color = Colors.RED
        val messageFormatter = MessageFormatter(mockk())

        val formattedMessage = messageFormatter.format(
                locale,
                color,
                "A: {0}, B: {1}",
                "Hi",
                MessageArguments.translate { throw RuntimeException("fail") }
        )

        assertThat(formattedMessage)
                .isEqualTo("A: {0}, B: {1}")
    }
}