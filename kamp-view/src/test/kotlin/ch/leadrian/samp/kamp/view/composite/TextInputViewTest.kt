package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.stubDefaultViewFactory
import ch.leadrian.samp.kamp.view.style.TextInputStyle
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.util.Locale

internal class TextInputViewTest {

    private lateinit var textInputView: TextInputView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()
    private val dialogService = mockk<DialogService>()
    private lateinit var viewFactory: ViewFactory

    @BeforeEach
    fun setUp() {
        viewFactory = stubDefaultViewFactory(
                viewContext = viewContext,
                textProvider = textProvider,
                textFormatter = textFormatter,
                dialogService = dialogService
        )
        textInputView = TextInputView(player, viewContext, viewFactory, dialogService, mockk())
    }

    @Nested
    inner class TitleTests {

        private val locale = Locale.GERMANY

        @BeforeEach
        fun setUp() {
            every { player.locale } returns locale
        }

        @Test
        fun shouldBeInitializedEmptyText() {
            val title = textInputView.title

            assertThat(title)
                    .isEqualTo(TextDrawCodes.EMPTY_TEXT)
        }

        @Test
        fun shouldSetTitle() {
            textInputView.title = "Hello there"

            assertThat(textInputView.title)
                    .isEqualTo("Hello there")
        }

        @Test
        fun shouldSupplyTitle() {
            textInputView.title { locale -> "Hello there in $locale" }

            assertThat(textInputView.title)
                    .isEqualTo("Hello there in de_DE")
        }

        @Test
        fun shouldSetTitleToFormattedString() {
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            textInputView.setTitle("Hi {0}, {1}", "there", 1234)

            assertThat(textInputView.title)
                    .isEqualTo("Hi there, 1234")
        }

        @Test
        fun shouldSetTitleToProvidedTitle() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi there"

            textInputView.setTitle(textKey)

            assertThat(textInputView.title)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldSetTitleToFormattedProvidedTitle() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi {0}, {1}"
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            textInputView.setTitle(textKey, "there", 1234)

            assertThat(textInputView.title)
                    .isEqualTo("Hi there, 1234")
        }

    }

    @ParameterizedTest
    @EnumSource(TextDrawFont::class)
    fun shouldSetTitleFont(font: TextDrawFont) {
        textInputView.titleFont = font

        assertThat(textInputView.titleFont)
                .isEqualTo(font)
    }

    @Test
    fun shouldSetTitleOutlineSize() {
        textInputView.titleOutlineSize = 69

        assertThat(textInputView.titleOutlineSize)
                .isEqualTo(69)
    }

    @Test
    fun shouldSetTitleShadowSize() {
        textInputView.titleShadowSize = 69

        assertThat(textInputView.titleShadowSize)
                .isEqualTo(69)
    }

    @Test
    fun shouldSetTitleBackgroundColor() {
        textInputView.titleBackgroundColor = Colors.PINK

        assertThat(textInputView.titleBackgroundColor)
                .isEqualTo(Colors.PINK)
    }

    @ParameterizedTest
    @EnumSource(TextDrawFont::class)
    fun shouldSetInputTextFont(font: TextDrawFont) {
        textInputView.inputTextFont = font

        assertThat(textInputView.inputTextFont)
                .isEqualTo(font)
    }

    @Test
    fun shouldSetInputTextOutlineSize() {
        textInputView.inputTextOutlineSize = 69

        assertThat(textInputView.inputTextOutlineSize)
                .isEqualTo(69)
    }

    @Test
    fun shouldSetInputTextShadowSize() {
        textInputView.inputTextShadowSize = 69

        assertThat(textInputView.inputTextShadowSize)
                .isEqualTo(69)
    }

    @Test
    fun shouldSetInputTextBackgroundColor() {
        textInputView.inputTextBackgroundColor = Colors.PINK

        assertThat(textInputView.inputTextBackgroundColor)
                .isEqualTo(Colors.PINK)
    }

    @Nested
    inner class ValidateTests {

        @Test
        fun givenTextInputIsNotRequiredAndInputTextIsNotSetItShouldSetStateToValid() {
            textInputView.isRequired = false

            textInputView.validate()

            assertThat(textInputView.state)
                    .isEqualTo(TextInputView.State.VALID)
        }

        @Test
        fun givenTextInputIsRequiredAndInputTextIsNotSetItShouldSetStateToInvalid() {
            textInputView.isRequired = true

            textInputView.validate()

            assertThat(textInputView.state)
                    .isEqualTo(TextInputView.State.INVALID)
        }

    }

    @Test
    fun shouldApplyTextInputStyle() {
        val style = object : TextInputStyle {

            override val invalidInputColor: Color = Colors.PINK

            override val inputTextFont: TextDrawFont = TextDrawFont.PRICEDOWN

            override val inputTextOutlineSize: Int = 3

            override val inputTextShadowSize: Int = 2

            override val inputTextColor: Color = Colors.YELLOW

            override val inputTextBackgroundColor: Color = Colors.GREEN

            override val disabledInputTextColor: Color = Colors.LIGHT_YELLOW

            override val disabledInputTextBackgroundColor: Color = Colors.LIGHT_PINK

            override val inputBackgroundColor: Color = Colors.LIGHT_BLUE

            override val titleFont: TextDrawFont = TextDrawFont.DIPLOMA

            override val titleOutlineSize: Int = 1

            override val titleShadowSize: Int = 4

            override val titleColor: Color = Colors.PURPLE

            override val titleBackgroundColor: Color = Colors.AQUAMARINE
        }

        textInputView.style(style)

        assertAll(
                { assertThat(textInputView.invalidInputColor).isEqualTo(Colors.PINK) },
                { assertThat(textInputView.inputTextFont).isEqualTo(TextDrawFont.PRICEDOWN) },
                { assertThat(textInputView.inputTextOutlineSize).isEqualTo(3) },
                { assertThat(textInputView.inputTextShadowSize).isEqualTo(2) },
                { assertThat(textInputView.inputTextColor).isEqualTo(Colors.YELLOW) },
                { assertThat(textInputView.inputTextBackgroundColor).isEqualTo(Colors.GREEN) },
                { assertThat(textInputView.disabledInputTextColor).isEqualTo(Colors.LIGHT_YELLOW) },
                { assertThat(textInputView.disabledInputTextBackgroundColor).isEqualTo(Colors.LIGHT_PINK) },
                { assertThat(textInputView.inputBackgroundColor).isEqualTo(Colors.LIGHT_BLUE) },
                { assertThat(textInputView.titleFont).isEqualTo(TextDrawFont.DIPLOMA) },
                { assertThat(textInputView.titleOutlineSize).isEqualTo(1) },
                { assertThat(textInputView.titleShadowSize).isEqualTo(4) },
                { assertThat(textInputView.titleColor).isEqualTo(Colors.PURPLE) },
                { assertThat(textInputView.titleBackgroundColor).isEqualTo(Colors.AQUAMARINE) }
        )
    }

}