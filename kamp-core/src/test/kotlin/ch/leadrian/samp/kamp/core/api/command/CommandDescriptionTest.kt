package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

internal class CommandDescriptionTest {

    private val textProvider = mockk<TextProvider>()

    @Test
    fun givenNoTextOrTextKeyItShouldReturnQuestionMarks() {
        val commandDescription = CommandDescription(text = null, textKey = null, textProvider = textProvider)

        val text = commandDescription.getText(Locale.GERMANY)

        assertThat(text)
                .isEqualTo("???")
    }

    @Test
    fun givenOnlyTextItShouldReturnText() {
        val commandDescription = CommandDescription(text = "Hi there", textKey = null, textProvider = textProvider)

        val text = commandDescription.getText(Locale.GERMANY)

        assertThat(text)
                .isEqualTo("Hi there")
    }

    @Test
    fun givenTextKeyItShouldReturnTranslatedText() {
        val locale = Locale.GERMANY
        val textKey = TextKey("test")
        every { textProvider.getText(locale, textKey, "Hi there") } returns "Hello"
        val commandDescription = CommandDescription(text = "Hi there", textKey = textKey, textProvider = textProvider)

        val text = commandDescription.getText(locale)

        assertThat(text)
                .isEqualTo("Hello")
    }

}