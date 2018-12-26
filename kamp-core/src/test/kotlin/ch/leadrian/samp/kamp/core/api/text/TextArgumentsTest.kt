package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*

internal class TextArgumentsTest {

    @ParameterizedTest
    @CsvSource(
            "de, DE, Hallo",
            "fr, FR, Bonjour",
            "it, IT, Ciao"
    )
    fun shouldReturnTranslatedText(language: String, country: String, expectedText: String) {
        val translations = mapOf(
                Locale.GERMANY to "Hallo",
                Locale.FRANCE to "Bonjour",
                Locale.ITALY to "Ciao"
        )
        val textArgument = TextArguments.translate { locale -> translations[locale]!! }

        val text = textArgument.getText(Locale(language, country))

        assertThat(text)
                .isEqualTo(expectedText)
    }

    @Test
    fun shouldReturnPlayerNameAndId() {
        val player = mockk<Player> {
            every { name } returns "hans_wurst"
            every { id } returns PlayerId.valueOf(1337)
        }
        val nameAndIdOf = TextArguments.nameAndIdOf(player)

        val text = nameAndIdOf.getText(Locale.GERMANY)

        assertThat(text)
                .isEqualTo("hans_wurst (1337)")
    }

}