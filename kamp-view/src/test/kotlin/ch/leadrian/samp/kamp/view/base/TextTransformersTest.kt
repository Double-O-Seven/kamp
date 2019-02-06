package ch.leadrian.samp.kamp.view.base

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.Locale
import java.util.stream.Stream

internal class TextTransformersTest {

    @Test
    fun shouldTransformToUpperCase() {
        val text = TextTransformers.toUpperCase().transform("Hi there", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("HI THERE")
    }

    @Test
    fun shouldTransformToLowerCase() {
        val text = TextTransformers.toLowerCase().transform("Hi There", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("hi there")
    }

    @Test
    fun shouldRemoveTilde() {
        val text = TextTransformers.removeTilde().transform("Hi~There~Honey!", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("HiThereHoney!")
    }

    @Test
    fun shouldReplaceTilde() {
        val text = TextTransformers.replaceTilde("+").transform("Hi~there~Honey!", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("Hi+there+Honey!")
    }

    @Test
    fun shouldFormatAtSign() {
        val text = TextTransformers.formatAtSign().transform("test@example.com", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("test(at)example.com")
    }

    @Test
    fun shouldTrim() {
        val text = TextTransformers.trim().transform("   Hi there    ", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("Hi there")
    }

    @Test
    fun shouldCombineTransformers() {
        val combiningTextTransformer = with(TextTransformers) {
            formatAtSign() andThen replaceTilde(" ") andThen trim() andThen toLowerCase()
        }
        val text = combiningTextTransformer.transform("~~Test@Example.com   ", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("test(at)example.com")
    }

    @ParameterizedTest
    @ArgumentsSource(PasswordizeArgumentsProvider::class)
    fun shouldPasswordize(inputText: String, expectedText: String) {
        val text = TextTransformers.passwordize().transform(inputText, Locale.GERMANY)

        assertThat(text)
                .isEqualTo(expectedText)
    }

    private class PasswordizeArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                    Arguments.of("ABC", "]]]"),
                    Arguments.of("", ""),
                    Arguments.of("ABCD".repeat(16), "]".repeat(64))
            )
        }

    }

}