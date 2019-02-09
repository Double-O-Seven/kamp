package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.stubDefaultViewFactory
import ch.leadrian.samp.kamp.view.style.ProgressBarStyle
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class ProgressBarViewTest {

    private lateinit var progressBarView: ProgressBarView
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
        progressBarView = ProgressBarView(player, viewContext, viewFactory)
    }

    @Nested
    inner class MaxValueTests {

        @Test
        fun shouldSetMaxValue() {
            progressBarView.maxValue = 1337

            assertThat(progressBarView.maxValue)
                    .isEqualTo(1337)
        }

        @Test
        fun shouldSupplyMaxValue() {
            progressBarView.maxValue { 1337 }

            assertThat(progressBarView.maxValue)
                    .isEqualTo(1337)
        }

    }

    @Nested
    inner class ValueTests {

        @Test
        fun shouldSetValue() {
            progressBarView.value = 1337

            assertThat(progressBarView.value)
                    .isEqualTo(1337)
        }

        @Test
        fun shouldSupplyValue() {
            progressBarView.value { 1337 }

            assertThat(progressBarView.value)
                    .isEqualTo(1337)
        }

    }

    @Nested
    inner class OutlineColorTests {

        @Test
        fun shouldBeInitializedWithBlack() {
            val outlineColor = progressBarView.outlineColor

            assertThat(outlineColor)
                    .isEqualTo(Colors.BLACK)
        }

        @Test
        fun shouldSetOutlineColor() {
            progressBarView.outlineColor = Colors.PINK

            assertThat(progressBarView.outlineColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyOutlineColor() {
            progressBarView.outlineColor { Colors.PINK }

            assertThat(progressBarView.outlineColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class SecondaryColorTests {

        @Test
        fun shouldBeInitializedWithDarkRed() {
            val secondaryColor = progressBarView.secondaryColor

            assertThat(secondaryColor)
                    .isEqualTo(Colors.DARK_RED)
        }

        @Test
        fun shouldSetSecondaryColor() {
            progressBarView.secondaryColor = Colors.PINK

            assertThat(progressBarView.secondaryColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplySecondaryColor() {
            progressBarView.secondaryColor { Colors.PINK }

            assertThat(progressBarView.secondaryColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class PrimaryColorTests {

        @Test
        fun shouldBeInitializedWithRed() {
            val primaryColor = progressBarView.primaryColor

            assertThat(primaryColor)
                    .isEqualTo(Colors.RED)
        }

        @Test
        fun shouldSetPrimaryColor() {
            progressBarView.primaryColor = Colors.PINK

            assertThat(progressBarView.primaryColor)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyPrimaryColor() {
            progressBarView.primaryColor { Colors.PINK }

            assertThat(progressBarView.primaryColor)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Test
    fun shouldApplyProgressBarStyle() {
        val style = object : ProgressBarStyle {

            override val progressBarPrimaryColor: Color = Colors.PINK

            override val progressBarSecondaryColor: Color = Colors.LIGHT_BLUE

            override val progressBarOutlineColor: Color = Colors.ORANGE
        }

        progressBarView.style(style)

        assertAll(
                { assertThat(progressBarView.primaryColor).isEqualTo(Colors.PINK) },
                { assertThat(progressBarView.secondaryColor).isEqualTo(Colors.LIGHT_BLUE) },
                { assertThat(progressBarView.outlineColor).isEqualTo(Colors.ORANGE) }
        )
    }

}