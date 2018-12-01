package ch.leadrian.samp.kamp.view

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

internal class ViewAreaCalculatorTest {

    private val player = mockk<Player>()
    private val viewAreaCalculator = ViewAreaCalculator()

    @Nested
    inner class NoParentTests {

        private val view = TestView(player, viewAreaCalculator)

        @Test
        fun shouldCalculateAreaWithoutDimensions() {
            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeWidth() {
            view.apply {
                width = 25f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 160f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteWidth() {
            view.width = 160f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 160f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeHeight() {
            view.height = 60f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 0f, 288f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteHeight() {
            view.height = 288f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 0f, 288f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeHorizontalMargins() {
            view.apply {
                marginLeft = 10f.percent()
                marginRight = 5f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(64f, 608f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteHorizontalMargins() {
            view.apply {
                marginLeft = 64f.pixels()
                marginRight = 32f.pixels()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(64f, 608f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeVerticalMargins() {
            view.apply {
                marginTop = 10f.percent()
                marginBottom = 5f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 456f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteVerticalMargins() {
            view.apply {
                marginTop = 48f.pixels()
                marginBottom = 24f.pixels()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 456f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeLeft() {
            view.left = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(64f, 704f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteLeft() {
            view.left = 64f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(64f, 704f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeRight() {
            view.right = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(-64f, 576f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteRight() {
            view.right = 64f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(-64f, 576f, 0f, 480f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeTop() {
            view.top = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 528f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteTop() {
            view.top = 48f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, 48f, 528f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeBottom() {
            view.bottom = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, -48f, 432f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteBottom() {
            view.bottom = 48f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(0f, 640f, -48f, 432f))
        }

    }

    @Nested
    inner class SingleParentTests {

        private val view = TestView(player, viewAreaCalculator)
        private val parentView = spyk(TestView(player, viewAreaCalculator))

        @BeforeEach
        fun setUp() {
            parentView.apply {
                addChild(view)
                every { area } returns rectangleOf(60f, 560f, 40f, 460f)
            }
        }

        @Test
        fun shouldCalculateAreaWithoutDimensions() {
            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeWidth() {
            view.apply {
                width = 25f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 185f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteWidth() {
            view.width = 160f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 220f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeHeight() {
            view.height = 60f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 292f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteHeight() {
            view.height = 288f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 328f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeHorizontalMargins() {
            view.apply {
                marginLeft = 10f.percent()
                marginRight = 5f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(110f, 535f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteHorizontalMargins() {
            view.apply {
                marginLeft = 64f.pixels()
                marginRight = 32f.pixels()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(124f, 528f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeVerticalMargins() {
            view.apply {
                marginTop = 10f.percent()
                marginBottom = 5f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 439f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteVerticalMargins() {
            view.apply {
                marginTop = 48f.pixels()
                marginBottom = 24f.pixels()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 436f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeLeft() {
            view.left = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(110f, 610f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteLeft() {
            view.left = 64f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(124f, 624f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeRight() {
            view.right = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(10f, 510f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteRight() {
            view.right = 64f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(-4f, 496f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeTop() {
            view.top = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 502f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteTop() {
            view.top = 48f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 508f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeBottom() {
            view.bottom = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -2f, 418f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteBottom() {
            view.bottom = 48f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -8f, 412f))
        }

    }

    @Nested
    inner class PaddingTests {

        private val view = TestView(player, viewAreaCalculator)
        private val parentView = spyk(TestView(player, viewAreaCalculator))

        @BeforeEach
        fun setUp() {
            parentView.apply {
                addChild(view)
                every { area } returns rectangleOf(50f, 580f, 10f, 500f)
                paddingLeft = 10.pixels()
                paddingRight = 20.pixels()
                paddingTop = 30.pixels()
                paddingBottom = 40.pixels()
            }
        }

        @Test
        fun shouldCalculateAreaWithoutDimensions() {
            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeWidth() {
            view.apply {
                width = 25f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 185f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteWidth() {
            view.width = 160f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 220f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeHeight() {
            view.height = 60f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 292f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteHeight() {
            view.height = 288f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 40f, 328f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeHorizontalMargins() {
            view.apply {
                marginLeft = 10f.percent()
                marginRight = 5f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(110f, 535f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteHorizontalMargins() {
            view.apply {
                marginLeft = 64f.pixels()
                marginRight = 32f.pixels()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(124f, 528f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeVerticalMargins() {
            view.apply {
                marginTop = 10f.percent()
                marginBottom = 5f.percent()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 439f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteVerticalMargins() {
            view.apply {
                marginTop = 48f.pixels()
                marginBottom = 24f.pixels()
            }

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 436f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeLeft() {
            view.left = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(110f, 610f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteLeft() {
            view.left = 64f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(124f, 624f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeRight() {
            view.right = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(10f, 510f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteRight() {
            view.right = 64f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(-4f, 496f, 40f, 460f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeTop() {
            view.top = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 82f, 502f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteTop() {
            view.top = 48f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, 88f, 508f))
        }

        @Test
        fun shouldCalculateScreenAreaWithRelativeBottom() {
            view.bottom = 10f.percent()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -2f, 418f))
        }

        @Test
        fun shouldCalculateScreenAreaWithAbsoluteBottom() {
            view.bottom = 48f.pixels()

            val area = viewAreaCalculator.calculate(view)

            assertThat(area)
                    .isEqualTo(mutableRectangleOf(60f, 560f, -8f, 412f))
        }

    }

    private val view = TestView(player, viewAreaCalculator)
    private val parentView = spyk(TestView(player, viewAreaCalculator))

    @BeforeEach
    fun setUp() {
        parentView.apply {
            addChild(view)
            every { area } returns rectangleOf(50f, 580f, 10f, 500f)
            paddingLeft = 10.pixels()
            paddingRight = 20.pixels()
            paddingTop = 30.pixels()
            paddingBottom = 40.pixels()
        }
    }

    @Test
    fun shouldApplyPaddingRelativeToGrandParent() {
        val view = TestView(player, viewAreaCalculator)
        val parentView = spyk(TestView(player, viewAreaCalculator)).apply {
            addChild(view)
            every { area } returns rectangleOf(10f, 620f, 30f, 440f)
            paddingLeft = 5.percent()
            paddingRight = 10.percent()
            paddingTop = 15.percent()
            paddingBottom = 20.percent()
        }
        spyk(TestView(player, viewAreaCalculator)).apply {
            addChild(parentView)
            every { area } returns SCREEN_AREA
        }
        val area = viewAreaCalculator.calculate(view)

        assertThat(area)
                .isEqualTo(mutableRectangleOf(42f, 556f, 102f, 344f))
    }

    private class TestView(
            player: Player,
            viewAreaCalculator: ViewAreaCalculator
    ) : View(player, viewAreaCalculator) {

        override fun onDraw() {}

    }

}