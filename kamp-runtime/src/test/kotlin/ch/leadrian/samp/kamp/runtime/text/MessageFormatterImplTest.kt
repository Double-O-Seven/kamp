package ch.leadrian.samp.kamp.runtime.text

import ch.leadrian.samp.kamp.api.data.Colors
import ch.leadrian.samp.kamp.api.text.HasTextKey
import ch.leadrian.samp.kamp.api.text.MessageArgument
import ch.leadrian.samp.kamp.api.text.TextKey
import ch.leadrian.samp.kamp.api.text.TextProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

internal class MessageFormatterImplTest {

    @Test
    fun shouldFormatMessage() {
        val locale = Locale.GERMANY
        val color = Colors.RED
        val textKey1 = TextKey("test.key.abc")
        val textKey2 = TextKey("test.key.def")
        val messageArgument = mockk<MessageArgument> {
            every { get(locale, color) } returns "Hi there"
        }
        val hasTextKey = mockk<HasTextKey> {
            every { this@mockk.textKey } returns textKey1
        }
        val textProvider = mockk<TextProvider> {
            every { getText(locale, textKey1) } returns "Hallo"
            every { getText(locale, textKey2) } returns "Bonjour"
        }
        val messageFormatter = MessageFormatterImpl(textProvider)

        val formattedMessage = messageFormatter.format(
                locale,
                color,
                "A: %d, B: %s, C: %s, D: %s",
                1337,
                messageArgument,
                hasTextKey,
                textKey2
        )

        assertThat(formattedMessage)
                .isEqualTo("A: 1337, B: Hi there, C: Hallo, D: Bonjour")
    }
}