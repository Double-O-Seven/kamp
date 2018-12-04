package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.view.View
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ViewLayoutCalculatorTest {

    @Test
    fun shouldCalculateLayout() {
        val parentArea = rectangleOf(1f, 2f, 3f, 4f)
        val contentArea = rectangleOf(10f, 20f, 30f, 40f)
        val paddingArea = rectangleOf(100f, 200f, 300f, 400f)
        val marginArea = rectangleOf(1000f, 2000f, 3000f, 4000f)
        val view = mockk<View> {
            every { this@mockk.parentArea } returns parentArea
        }
        val absoluteViewDimensions = AbsoluteViewDimensions(
                width = 1f,
                height = 2f,
                left = 3f,
                right = 4f,
                top = 5f,
                bottom = 6f,
                marginLeft = 7f,
                marginRight = 8f,
                marginTop = 9f,
                marginBottom = 10f,
                paddingLeft = 11f,
                paddingRight = 12f,
                paddingTop = 13f,
                paddingBottom = 14f
        )
        val absoluteViewDimensionsCalculator = mockk<AbsoluteViewDimensionsCalculator> {
            every { calculate(view) } returns absoluteViewDimensions
        }
        val viewAreaCalculator = mockk<ViewAreaCalculator> {
            every { calculateContentArea(parentArea, absoluteViewDimensions) } returns contentArea
            every { calculatePaddingArea(contentArea, absoluteViewDimensions) } returns paddingArea
            every { calculateMarginArea(paddingArea, absoluteViewDimensions) } returns marginArea
        }
        val viewLayoutCalculator = ViewLayoutCalculator(absoluteViewDimensionsCalculator, viewAreaCalculator)

        val layout = viewLayoutCalculator.calculate(view)

        assertThat(layout)
                .isEqualTo(ViewLayout(
                        marginArea = marginArea,
                        paddingArea = paddingArea,
                        contentArea = contentArea
                ))
    }

}