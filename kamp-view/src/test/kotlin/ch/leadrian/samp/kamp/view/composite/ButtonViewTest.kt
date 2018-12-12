package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class ButtonViewTest {

    private lateinit var buttonView: ButtonView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()
    private lateinit var viewFactory: ViewFactory

    @BeforeEach
    fun setUp() {
        viewFactory = DefaultViewFactory(viewContext, textProvider, textFormatter, playerTextDrawService)
        buttonView = ButtonView(player, viewContext, viewFactory)
    }

    @Nested
    inner class TextColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val textColor = buttonView.textColor

            assertThat(textColor)
                    .isEqualTo(Colors.BLACK)
        }

        @Test
        fun shouldSetColor() {
            buttonView.textColor = Colors.PINK

            assertThat(buttonView.textColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            buttonView.textColor { Colors.PINK }

            assertThat(buttonView.textColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class DisabledTextColorTests {

        @Test
        fun shouldBeInitializedWithDarkGrey() {
            val disabledTextColor = buttonView.disabledTextColor

            assertThat(disabledTextColor)
                    .isEqualTo(Colors.DARK_GRAY)
        }

        @Test
        fun shouldSetColor() {
            buttonView.disabledTextColor = Colors.PINK

            assertThat(buttonView.disabledTextColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            buttonView.disabledTextColor { Colors.PINK }

            assertThat(buttonView.disabledTextColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class TextBackgroundColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val textBackgroundColor = buttonView.textBackgroundColor

            assertThat(textBackgroundColor)
                    .isEqualTo(Colors.BLACK)
        }

        @Test
        fun shouldSetColor() {
            buttonView.textBackgroundColor = Colors.PINK

            assertThat(buttonView.textBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            buttonView.textBackgroundColor { Colors.PINK }

            assertThat(buttonView.textBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class DisabledTextBackgroundColorTests {

        @Test
        fun shouldBeInitializedWithDarkGrey() {
            val disabledTextBackgroundColor = buttonView.disabledTextBackgroundColor

            assertThat(disabledTextBackgroundColor)
                    .isEqualTo(Colors.DARK_GRAY)
        }

        @Test
        fun shouldSetColor() {
            buttonView.disabledTextBackgroundColor = Colors.PINK

            assertThat(buttonView.disabledTextBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            buttonView.disabledTextBackgroundColor { Colors.PINK }

            assertThat(buttonView.disabledTextBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class BackgroundColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val backgroundColor = buttonView.backgroundColor

            assertThat(backgroundColor)
                    .isEqualTo(Colors.GREY)
        }

        @Test
        fun shouldSetBackgroundColor() {
            buttonView.backgroundColor = Colors.PINK

            assertThat(buttonView.backgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyBackgroundColor() {
            buttonView.backgroundColor { Colors.PINK }

            assertThat(buttonView.backgroundColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class DisabledBackgroundColorTests {

        @Test
        fun shouldBeInitializedWithLightGrey() {
            val disabledBackgroundColor = buttonView.disabledBackgroundColor

            assertThat(disabledBackgroundColor)
                    .isEqualTo(Colors.LIGHT_GRAY)
        }

        @Test
        fun shouldSetDisabledBackgroundColor() {
            buttonView.disabledBackgroundColor = Colors.PINK

            assertThat(buttonView.disabledBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyDisabledBackgroundColor() {
            buttonView.disabledBackgroundColor { Colors.PINK }

            assertThat(buttonView.disabledBackgroundColor)
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
            val text = buttonView.text

            assertThat(text)
                    .isEqualTo(TextDrawCodes.EMPTY_TEXT)
        }

        @Test
        fun shouldSetText() {
            buttonView.text = "Hello there"

            assertThat(buttonView.text)
                    .isEqualTo("Hello there")
        }

        @Test
        fun shouldSupplyText() {
            buttonView.text { locale -> "Hello there in $locale" }

            assertThat(buttonView.text)
                    .isEqualTo("Hello there in de_DE")
        }

        @Test
        fun shouldSetTextToFormattedString() {
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            buttonView.setText("Hi {0}, {1}", "there", 1234)

            assertThat(buttonView.text)
                    .isEqualTo("Hi there, 1234")
        }

        @Test
        fun shouldSetTextToProvidedText() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi there"

            buttonView.setText(textKey)

            assertThat(buttonView.text)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldSetTextToFormattedProvidedText() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi {0}, {1}"
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            buttonView.setText(textKey, "there", 1234)

            assertThat(buttonView.text)
                    .isEqualTo("Hi there, 1234")
        }

    }

}
