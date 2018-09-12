package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class CommandAccessCheckerGroupTest {

    private val locale = Locale.GERMANY
    private val textProvider = mockk<TextProvider>()

    @Test
    fun givenNoTextKeyAndNoTextItShouldReturnNull() {
        val commandAccessCheckerGroup = CommandAccessCheckerGroup(
                accessCheckers = emptyList(),
                accessDeniedHandlers = emptyList(),
                errorMessage = null,
                errorMessageTextKey = null,
                textProvider = textProvider
        )

        val errorMessage = commandAccessCheckerGroup.getErrorMessage(locale)

        assertThat(errorMessage)
                .isNull()
    }

    @Test
    fun givenTextAndNoTextKeyItShouldReturnTextAsErrorMessage() {
        val commandAccessCheckerGroup = CommandAccessCheckerGroup(
                accessCheckers = emptyList(),
                accessDeniedHandlers = emptyList(),
                errorMessage = "test",
                errorMessageTextKey = null,
                textProvider = textProvider
        )

        val errorMessage = commandAccessCheckerGroup.getErrorMessage(locale)

        assertThat(errorMessage)
                .isEqualTo("test")
    }

    @Test
    fun givenTextKeyItShouldReturnTranslatedErrorMessage() {
        val errorMessageTextKey = TextKey("command.definition.test")
        every { textProvider.getText(locale, errorMessageTextKey, "test") } returns "Hi there"
        val commandAccessCheckerGroup = CommandAccessCheckerGroup(
                accessCheckers = emptyList(),
                accessDeniedHandlers = emptyList(),
                errorMessage = "test",
                errorMessageTextKey = errorMessageTextKey,
                textProvider = textProvider
        )

        val errorMessage = commandAccessCheckerGroup.getErrorMessage(locale)

        assertThat(errorMessage)
                .isEqualTo("Hi there")
    }

}