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
        val textProvider = mockk<TextProvider> {
            every { getText(locale, textKey1) } returns "Hallo"
            every { getText(locale, textKey2) } returns "Bonjour"
        }
        val textFormatter = TextFormatter(textProvider)

        val formattedText = textFormatter.format(
                locale,
                "A: %d, B: %s, C: %s",
                1337,
                hasTextKey,
                textKey2
        )

        assertThat(formattedText)
                .isEqualTo("A: 1337, B: Hallo, C: Bonjour")
    }
}