package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*

internal class MessageArgumentsTest {

    @Test
    fun shouldReturnColoredPlayerName() {
        val player = mockk<Player> {
            every { color } returns colorOf(0x00FF00FF)
            every { name } returns "hans_wurst"
        }
        val embeddedPlayerName = coloredNameOf(player)

        val text = embeddedPlayerName.getText(Locale.GERMANY, colorOf(0x33CC33FF))

        assertThat(text)
                .isEqualTo("{00ff00}hans_wurst{33cc33}")
    }

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
        val messageArgument = translateForMessage { locale -> translations[locale]!! }

        val text = messageArgument.getText(Locale(language, country), Colors.WHITE)

        assertThat(text)
                .isEqualTo(expectedText)
    }

}