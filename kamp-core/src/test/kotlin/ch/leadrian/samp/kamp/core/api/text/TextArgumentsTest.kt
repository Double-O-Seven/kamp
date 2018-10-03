package ch.leadrian.samp.kamp.core.api.text

import org.assertj.core.api.Assertions
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
        val textArgument = translateForText { locale -> translations[locale]!! }

        val text = textArgument.get(Locale(language, country))

        Assertions.assertThat(text)
                .isEqualTo(expectedText)
    }

}