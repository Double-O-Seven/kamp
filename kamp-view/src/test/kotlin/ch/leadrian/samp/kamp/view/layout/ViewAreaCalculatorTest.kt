package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ViewAreaCalculatorTest {

    private val viewAreaCalculator = ViewAreaCalculator()

    @Nested
    inner class CalculateContentAreaTests {

        @Test
        fun givenWidthAndHeightAndLeftAndTopItShouldCalculateContentAreaIgnoringRightAndBottom() {
            val parentArea = rectangleOf(200f, 1000f, 3000f, 5000f)
            val absoluteViewDimensions = AbsoluteViewDimensions(
                    width = 15f,
                    height = 20f,
                    left = 100f,
                    right = 200f,
                    top = 300f,
                    bottom = 400f,
                    marginLeft = 3f,
                    marginRight = 6f,
                    marginTop = 9f,
                    marginBottom = 12f,
                    paddingLeft = 11f,
                    paddingRight = 22f,
                    paddingTop = 33f,
                    paddingBottom = 44f
            )

            val contentArea = viewAreaCalculator.calculateContentArea(parentArea, absoluteViewDimensions)

            assertThat(contentArea)
                    .isEqualTo(
                            rectangleOf(
                                    minX = 200f + 100f + 3f + 11f,
                                    maxX = 200f + 100f + 3f + 11f + 15f,
                                    minY = 3000f + 300f + 9f + 33f,
                                    maxY = 3000f + 300f + 9f + 33f + 20f
                            )
                    )
        }

        @Test
        fun givenWidthAndHeightOnlyItShouldCalculateContentAreaUsingZeroForLeftAndTop() {
            val parentArea = rectangleOf(200f, 1000f, 3000f, 5000f)
            val absoluteViewDimensions = AbsoluteViewDimensions(
                    width = 15f,
                    height = 20f,
                    left = null,
                    right = null,
                    top = null,
                    bottom = null,
                    marginLeft = 3f,
                    marginRight = 6f,
                    marginTop = 9f,
                    marginBottom = 12f,
                    paddingLeft = 11f,
                    paddingRight = 22f,
                    paddingTop = 33f,
                    paddingBottom = 44f
            )

            val contentArea = viewAreaCalculator.calculateContentArea(parentArea, absoluteViewDimensions)

            assertThat(contentArea)
                    .isEqualTo(
                            rectangleOf(
                                    minX = 200f + 3f + 11f,
                                    maxX = 200f + 3f + 11f + 15f,
                                    minY = 3000f + 9f + 33f,
                                    maxY = 3000f + 9f + 33f + 20f
                            )
                    )
        }

        @Test
        fun givenWidthAndHeightAndRightAndBottomItShouldCalculateContentArea() {
            val parentArea = rectangleOf(200f, 1000f, 3000f, 5000f)
            val absoluteViewDimensions = AbsoluteViewDimensions(
                    width = 15f,
                    height = 20f,
                    left = null,
                    right = 200f,
                    top = null,
                    bottom = 400f,
                    marginLeft = 3f,
                    marginRight = 6f,
                    marginTop = 9f,
                    marginBottom = 12f,
                    paddingLeft = 11f,
                    paddingRight = 22f,
                    paddingTop = 33f,
                    paddingBottom = 44f
            )

            val contentArea = viewAreaCalculator.calculateContentArea(parentArea, absoluteViewDimensions)

            assertThat(contentArea)
                    .isEqualTo(
                            rectangleOf(
                                    minX = 1000f - 200f - 6f - 22f - 15f,
                                    maxX = 1000f - 200f - 6f - 22f,
                                    minY = 5000f - 400f - 12f - 44f - 20f,
                                    maxY = 5000f - 400f - 12f - 44f
                            )
                    )
        }

        @Test
        fun givenNoWidthAndNoHeightItShouldCalculateContentAreaUsingRightAndBottom() {
            val parentArea = rectangleOf(200f, 1000f, 3000f, 5000f)
            val absoluteViewDimensions = AbsoluteViewDimensions(
                    width = null,
                    height = null,
                    left = 100f,
                    right = 200f,
                    top = 300f,
                    bottom = 400f,
                    marginLeft = 3f,
                    marginRight = 6f,
                    marginTop = 9f,
                    marginBottom = 12f,
                    paddingLeft = 11f,
                    paddingRight = 22f,
                    paddingTop = 33f,
                    paddingBottom = 44f
            )

            val contentArea = viewAreaCalculator.calculateContentArea(parentArea, absoluteViewDimensions)

            assertThat(contentArea)
                    .isEqualTo(
                            rectangleOf(
                                    minX = 200f + 100f + 3f + 11f,
                                    maxX = 1000f - 200f - 6f - 22f,
                                    minY = 3000f + 300f + 9f + 33f,
                                    maxY = 5000f - 400f - 12f - 44f
                            )
                    )
        }

        @Test
        fun givenMinimalDataItShouldFallbackToZeroAsDefaultValues() {
            val parentArea = rectangleOf(200f, 1000f, 3000f, 5000f)
            val absoluteViewDimensions = AbsoluteViewDimensions(
                    width = null,
                    height = null,
                    left = null,
                    right = null,
                    top = null,
                    bottom = null,
                    marginLeft = 3f,
                    marginRight = 6f,
                    marginTop = 9f,
                    marginBottom = 12f,
                    paddingLeft = 11f,
                    paddingRight = 22f,
                    paddingTop = 33f,
                    paddingBottom = 44f
            )

            val contentArea = viewAreaCalculator.calculateContentArea(parentArea, absoluteViewDimensions)

            assertThat(contentArea)
                    .isEqualTo(
                            rectangleOf(
                                    minX = 200f + 3f + 11f,
                                    maxX = 1000f - 6f - 22f,
                                    minY = 3000f + 9f + 33f,
                                    maxY = 5000f - 12f - 44f
                            )
                    )
        }
    }

    @Test
    fun shouldCalculatePaddingArea() {
        val contentArea = rectangleOf(200f, 1000f, 3000f, 5000f)
        val absoluteViewDimensions = AbsoluteViewDimensions(
                width = null,
                height = null,
                left = null,
                right = null,
                top = null,
                bottom = null,
                marginLeft = 3f,
                marginRight = 6f,
                marginTop = 9f,
                marginBottom = 12f,
                paddingLeft = 11f,
                paddingRight = 22f,
                paddingTop = 33f,
                paddingBottom = 44f
        )

        val paddingArea = viewAreaCalculator.calculatePaddingArea(contentArea, absoluteViewDimensions)

        assertThat(paddingArea)
                .isEqualTo(
                        rectangleOf(
                                minX = 200f - 11f,
                                maxX = 1000f + 22f,
                                minY = 3000f - 33f,
                                maxY = 5000f + 44f
                        )
                )
    }

    @Test
    fun shouldCalculateMarginArea() {
        val paddingArea = rectangleOf(200f, 1000f, 3000f, 5000f)
        val absoluteViewDimensions = AbsoluteViewDimensions(
                width = null,
                height = null,
                left = null,
                right = null,
                top = null,
                bottom = null,
                marginLeft = 11f,
                marginRight = 22f,
                marginTop = 33f,
                marginBottom = 44f,
                paddingLeft = 3f,
                paddingRight = 6f,
                paddingTop = 9f,
                paddingBottom = 12f
        )

        val marginArea = viewAreaCalculator.calculateMarginArea(paddingArea, absoluteViewDimensions)

        assertThat(marginArea)
                .isEqualTo(
                        rectangleOf(
                                minX = 200f - 11f,
                                maxX = 1000f + 22f,
                                minY = 3000f - 33f,
                                maxY = 5000f + 44f
                        )
                )
    }

}