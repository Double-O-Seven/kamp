package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.mutableRectangleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ViewLayoutCalculatorTest {

    private val player = mockk<Player>()
    private val layoutCalculator = ViewLayoutCalculator()

    @Nested
    inner class NoParentTests {

        private val view = TestView(player, layoutCalculator)

        @Test
        fun shouldCalculateLayoutWithoutDimensions() {
            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeWidth() {
            view.apply {
                width = 25f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 160f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteWidth() {
            view.width = 160f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 160f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeHeight() {
            view.height = 60f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 0f, 288f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteHeight() {
            view.height = 288f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 0f, 288f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeHorizontalMargins() {
            view.apply {
                marginLeft = 10f.percent()
                marginRight = 5f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(64f, 608f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteHorizontalMargins() {
            view.apply {
                marginLeft = 64f.pixels()
                marginRight = 32f.pixels()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(64f, 608f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeVerticalMargins() {
            view.apply {
                marginTop = 10f.percent()
                marginBottom = 5f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 456f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteVerticalMargins() {
            view.apply {
                marginTop = 48f.pixels()
                marginBottom = 24f.pixels()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 456f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeLeft() {
            view.left = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(64f, 704f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteLeft() {
            view.left = 64f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(64f, 704f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeRight() {
            view.right = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(-64f, 576f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteRight() {
            view.right = 64f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(-64f, 576f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeTop() {
            view.top = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 528f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteTop() {
            view.top = 48f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 528f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeBottom() {
            view.bottom = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, -48f, 432f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteBottom() {
            view.bottom = 48f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(0f, 640f, -48f, 432f))
        }

    }

    @Nested
    inner class SingleParentTests {

        private val view = TestView(player, layoutCalculator)
        private val parentView = spyk(TestView(player, layoutCalculator))

        @BeforeEach
        fun setUp() {
            parentView.apply {
                addChild(view)
                every { layout } returns rectangleOf(60f, 560f, 40f, 460f)
            }
        }

        @Test
        fun shouldCalculateLayoutWithoutDimensions() {
            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeWidth() {
            view.apply {
                width = 25f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 185f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteWidth() {
            view.width = 160f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 220f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeHeight() {
            view.height = 60f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 292f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteHeight() {
            view.height = 288f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 328f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeHorizontalMargins() {
            view.apply {
                marginLeft = 10f.percent()
                marginRight = 5f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(110f, 535f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteHorizontalMargins() {
            view.apply {
                marginLeft = 64f.pixels()
                marginRight = 32f.pixels()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(124f, 528f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeVerticalMargins() {
            view.apply {
                marginTop = 10f.percent()
                marginBottom = 5f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 439f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteVerticalMargins() {
            view.apply {
                marginTop = 48f.pixels()
                marginBottom = 24f.pixels()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 436f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeLeft() {
            view.left = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(110f, 610f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteLeft() {
            view.left = 64f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(124f, 624f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeRight() {
            view.right = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(10f, 510f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteRight() {
            view.right = 64f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(-4f, 496f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeTop() {
            view.top = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 502f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteTop() {
            view.top = 48f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 508f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeBottom() {
            view.bottom = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -2f, 418f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteBottom() {
            view.bottom = 48f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -8f, 412f))
        }

    }

    @Nested
    inner class PaddingTests {

        private val view = TestView(player, layoutCalculator)
        private val parentView = spyk(TestView(player, layoutCalculator))

        @BeforeEach
        fun setUp() {
            parentView.apply {
                addChild(view)
                every { layout } returns rectangleOf(50f, 580f, 10f, 500f)
                paddingLeft = 10.pixels()
                paddingRight = 20.pixels()
                paddingTop = 30.pixels()
                paddingBottom = 40.pixels()
            }
        }

        @Test
        fun shouldCalculateLayoutWithoutDimensions() {
            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeWidth() {
            view.apply {
                width = 25f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 185f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteWidth() {
            view.width = 160f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 220f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeHeight() {
            view.height = 60f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 292f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteHeight() {
            view.height = 288f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 328f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeHorizontalMargins() {
            view.apply {
                marginLeft = 10f.percent()
                marginRight = 5f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(110f, 535f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteHorizontalMargins() {
            view.apply {
                marginLeft = 64f.pixels()
                marginRight = 32f.pixels()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(124f, 528f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeVerticalMargins() {
            view.apply {
                marginTop = 10f.percent()
                marginBottom = 5f.percent()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 439f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteVerticalMargins() {
            view.apply {
                marginTop = 48f.pixels()
                marginBottom = 24f.pixels()
            }

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 436f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeLeft() {
            view.left = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(110f, 610f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteLeft() {
            view.left = 64f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(124f, 624f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeRight() {
            view.right = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(10f, 510f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteRight() {
            view.right = 64f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(-4f, 496f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeTop() {
            view.top = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 502f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteTop() {
            view.top = 48f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 508f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithRelativeBottom() {
            view.bottom = 10f.percent()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -2f, 418f))
        }

        @Test
        fun shouldCalculateScreenLayoutWithAbsoluteBottom() {
            view.bottom = 48f.pixels()

            val layout = layoutCalculator.calculate(view)

            assertThat(layout)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -8f, 412f))
        }

    }

    private val view = TestView(player, layoutCalculator)
    private val parentView = spyk(TestView(player, layoutCalculator))

    @BeforeEach
    fun setUp() {
        parentView.apply {
            addChild(view)
            every { layout } returns rectangleOf(50f, 580f, 10f, 500f)
            paddingLeft = 10.pixels()
            paddingRight = 20.pixels()
            paddingTop = 30.pixels()
            paddingBottom = 40.pixels()
        }
    }

    @Test
    fun shouldApplyPaddingRelativeToGrandParent() {
        val view = TestView(player, layoutCalculator)
        val parentView = spyk(TestView(player, layoutCalculator)).apply {
            addChild(view)
            every { layout } returns rectangleOf(10f, 620f, 30f, 440f)
            paddingLeft = 5.percent()
            paddingRight = 10.percent()
            paddingTop = 15.percent()
            paddingBottom = 20.percent()
        }
        spyk(TestView(player, layoutCalculator)).apply {
            addChild(parentView)
            every { layout } returns SCREEN_LAYOUT
        }
        val layout = layoutCalculator.calculate(view)

        assertThat(layout)
                .isEqualTo(mutableRectangleOf(42f, 556f, 102f, 344f))
    }

    private class TestView(
            player: Player,
            viewLayoutCalculator: ViewLayoutCalculator
    ) : View(player, viewLayoutCalculator) {

        override fun draw(layout: Rectangle) {}

        override fun onShow() {}

        override fun onHide() {}

    }

}