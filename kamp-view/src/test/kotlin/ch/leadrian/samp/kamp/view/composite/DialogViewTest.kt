package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.pixels
import ch.leadrian.samp.kamp.view.style.DialogStyle
import ch.leadrian.samp.kamp.view.style.Style
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class DialogViewTest {

    private lateinit var dialogView: DialogView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()
    private val dialogService = mockk<DialogService>()
    private lateinit var viewFactory: ViewFactory

    @BeforeEach
    fun setUp() {
        viewFactory = DefaultViewFactory(viewContext, textProvider, textFormatter, playerTextDrawService, dialogService)
        dialogView = DialogView(player, viewContext, viewFactory)
    }

    @Nested
    inner class TitleBarColorTests {

        @Test
        fun shouldBeInitializedWithGrey() {
            val titleBarColor = dialogView.titleBarColor

            assertThat(titleBarColor)
                    .isEqualTo(Colors.GREY)
        }

        @Test
        fun shouldSetColor() {
            dialogView.titleBarColor = Colors.PINK

            assertThat(dialogView.titleBarColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            dialogView.titleBarColor { Colors.PINK }

            assertThat(dialogView.titleBarColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class TitleColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val titleColor = dialogView.titleColor

            assertThat(titleColor)
                    .isEqualTo(Colors.BLACK)
        }

        @Test
        fun shouldSetColor() {
            dialogView.titleColor = Colors.PINK

            assertThat(dialogView.titleColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            dialogView.titleColor { Colors.PINK }

            assertThat(dialogView.titleColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class TitleBackgroundColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val titleBackgroundColor = dialogView.titleBackgroundColor

            assertThat(titleBackgroundColor)
                    .isEqualTo(Colors.BLACK)
        }

        @Test
        fun shouldSetColor() {
            dialogView.titleBackgroundColor = Colors.PINK

            assertThat(dialogView.titleBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            dialogView.titleBackgroundColor { Colors.PINK }

            assertThat(dialogView.titleBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class ContentBackgroundColorTests {

        @Test
        fun shouldBeInitializedWithTransparentBlack() {
            val backgroundColor = dialogView.contentBackgroundColor

            assertThat(backgroundColor)
                    .isEqualTo(colorOf(0x00000080))
        }

        @Test
        fun shouldSetColor() {
            dialogView.contentBackgroundColor = Colors.PINK

            assertThat(dialogView.contentBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            dialogView.contentBackgroundColor { Colors.PINK }

            assertThat(dialogView.contentBackgroundColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class TitleTests {

        private val locale = Locale.GERMANY

        @BeforeEach
        fun setUp() {
            every { player.locale } returns locale
        }

        @Test
        fun shouldBeInitializedWithWhite() {
            val text = dialogView.title

            assertThat(text)
                    .isEqualTo(TextDrawCodes.EMPTY_TEXT)
        }

        @Test
        fun shouldSetTitle() {
            dialogView.title = "Hello there"

            assertThat(dialogView.title)
                    .isEqualTo("Hello there")
        }

        @Test
        fun shouldSupplyTitle() {
            dialogView.title { locale -> "Hello there in $locale" }

            assertThat(dialogView.title)
                    .isEqualTo("Hello there in de_DE")
        }

        @Test
        fun shouldSetTitleToFormattedString() {
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            dialogView.setTitle("Hi {0}, {1}", "there", 1234)

            assertThat(dialogView.title)
                    .isEqualTo("Hi there, 1234")
        }

        @Test
        fun shouldSetTitleToProvidedText() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi there"

            dialogView.setTitle(textKey)

            assertThat(dialogView.title)
                    .isEqualTo("Hi there")
        }

        @Test
        fun shouldSetTitleToFormattedProvidedText() {
            val textKey = TextKey("view.test")
            every { textProvider.getText(locale, textKey) } returns "Hi {0}, {1}"
            every { textFormatter.format(locale, "Hi {0}, {1}", "there", 1234) } returns "Hi there, 1234"

            dialogView.setTitle(textKey, "there", 1234)

            assertThat(dialogView.title)
                    .isEqualTo("Hi there, 1234")
        }

    }

    @Nested
    inner class StyleTests {

        @Test
        fun shouldNotApplyStyleToChildren() {
            val style = object : Style {}
            val childView = mockk<View>(relaxed = true)
            dialogView.addChild(childView)

            dialogView.style(style)

            verify(exactly = 0) { childView.style(any()) }
        }

        @Test
        fun shouldApplyDialogStyle() {
            val dialogStyle = object : DialogStyle {
                override val dialogContentBackgroundColor: Color
                    get() = Colors.PINK
                override val dialogTitleBackgroundColor: Color
                    get() = Colors.CYAN
                override val dialogTitleBarColor: Color
                    get() = Colors.LIGHT_BLUE
                override val dialogTitleBarHeight: ViewDimension
                    get() = 69.pixels()
                override val dialogTitleBarPadding: ViewDimension
                    get() = 33.pixels()
                override val dialogTitleColor: Color
                    get() = Colors.ALICE_BLUE
                override val dialogTitleFont: TextDrawFont
                    get() = TextDrawFont.PRICEDOWN
                override val dialogTitleOutlineSize: Int
                    get() = 13
                override val dialogTitleShadowSize: Int
                    get() = 7
            }

            dialogView.style(dialogStyle)

            assertThat(dialogView)
                    .satisfies {
                        assertThat(it.contentBackgroundColor)
                                .isEqualTo(Colors.PINK)
                        assertThat(it.titleBackgroundColor)
                                .isEqualTo(Colors.CYAN)
                        assertThat(it.titleBarColor)
                                .isEqualTo(Colors.LIGHT_BLUE)
                        assertThat(it.titleBarHeight)
                                .isEqualTo(69.pixels())
                        assertThat(it.titleBarPadding)
                                .isEqualTo(33.pixels())
                        assertThat(it.titleColor)
                                .isEqualTo(Colors.ALICE_BLUE)
                        assertThat(it.titleFont)
                                .isEqualTo(TextDrawFont.PRICEDOWN)
                        assertThat(it.titleOutlineSize)
                                .isEqualTo(13)
                        assertThat(it.titleShadowSize)
                                .isEqualTo(7)
                    }
        }

    }

}