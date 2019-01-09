package ch.leadrian.samp.kamp.view.base

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

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
            formatAtSign().andThen(replaceTilde(" ")).andThen(trim()).andThen(toLowerCase())
        }
        val text = combiningTextTransformer.transform("~~Test@Example.com   ", Locale.GERMANY)

        assertThat(text)
                .isEqualTo("test(at)example.com")
    }

}