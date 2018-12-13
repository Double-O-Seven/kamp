package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class TextViewTest {

    private lateinit var textView: TextView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()

    @BeforeEach
    fun setUp() {
        textView = TextView(player, viewContext, textProvider, textFormatter, playerTextDrawService)
    }

    @Nested
    inner class ColorTests {

        @Test
        fun shouldBeInitializedWithWhite() {
            val color = textView.color

            assertThat(color)
                    .isEqualTo(Colors.WHITE)
        }

        @Test
        fun shouldSetColor() {
            textView.color = Colors.PINK

            assertThat(textView.color)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            textView.color { Colors.PINK }

            assertThat(textView.color)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class BackgroundColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val backgroundColor = textView.backgroundColor

            assertThat(backgroundColor)
                    .isEqualTo(Colors.BLACK)
        }

        @Test
        fun shouldSetBackgroundColor() {
            textView.backgroundColor = Colors.PINK

            assertThat(textView.backgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyBackgroundColor() {
            textView.backgroundColor { Colors.PINK }

            assertThat(textView.backgroundColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class TextTests {

        private val locale = Locale.GERMANY

        @BeforeEach
        fun setUp() {
            every { player.locale } returns locale
        }

        @Test
        fun shouldBeInitializedWithWhite() {
            val text = textView.text

            assertThat(text)
                    .isEqualTo(TextDrawCodes.EMPTY_TEXT)
        }

        @Test
        fun shouldSetText() {
            textView.text = "Hello there"

            assertThat(textView.text)
                    .isEqualTo("Hello there")
        }

        @Test
        fun shouldSupplyText() {
            textView.text { locale -> "Hello there in $locale" }

            assertThat(textView.text)
                    .isEqualTo("Hello there in de_DE")
        }

        @Test
        fun shouldSetTextToFormattedString() {
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            textView.setText("Hi {0}, {1}", "there", 1234)

            assertThat(textView.text)
                    .isEqualTo("Hi there, 1234")
        }

        @Test
        fun shouldSetTextToProvidedText() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi there"

            textView.setText(textKey)

            assertThat(textView.text)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldSetTextToFormattedProvidedText() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi {0}, {1}"
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            textView.setText(textKey, "there", 1234)

            assertThat(textView.text)
                    .isEqualTo("Hi there, 1234")
        }

        @Test
        fun shouldReplaceBlankTextWithEmptyTextDraw() {
            textView.text = "    \n    "

            val text = textView.text

            assertThat(text)
                    .isEqualTo("_")
        }

        @Test
        fun shouldTrimToMaximumTextDrawTextLength() {
            textView.text = "x".repeat(2000)

            val text = textView.text

            assertThat(text)
                    .isEqualToIgnoringCase("x".repeat(1023))
        }

        @Test
        fun givenTextTransformerItShouldTransformText() {
            textView.textTransformer = TextTransformers.toUpperCase()
            textView.text = "Hi there"

            val text = textView.text

            assertThat(text)
                    .isEqualTo("HI THERE")
        }

    }

}