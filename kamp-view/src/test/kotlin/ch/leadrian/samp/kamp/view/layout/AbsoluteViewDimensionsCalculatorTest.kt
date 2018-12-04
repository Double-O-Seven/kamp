package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.view.View
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AbsoluteViewDimensionsCalculatorTest {

    private val absoluteViewDimensionsCalculator = AbsoluteViewDimensionsCalculator()
    private lateinit var view: View

    @BeforeEach
    fun setUp() {
        view = spyk(View(mockk(), mockk()))
    }

    @Test
    fun givenAbsoluteDimensionsItShouldCalculateValues() {
        view.apply {
            width = 10.pixels()
            height = 20.pixels()
            left = 30.pixels()
            right = 40.pixels()
            top = 50.pixels()
            bottom = 60.pixels()
            marginLeft = 70.pixels()
            marginRight = 80.pixels()
            marginTop = 90.pixels()
            marginBottom = 100.pixels()
            paddingLeft = 110.pixels()
            paddingRight = 120.pixels()
            paddingTop = 130.pixels()
            paddingBottom = 140.pixels()
        }

        val dimensions = absoluteViewDimensionsCalculator.calculate(view)

        assertThat(dimensions)
                .isEqualTo(AbsoluteViewDimensions(
                        width = 10f,
                        height = 20f,
                        left = 30f,
                        right = 40f,
                        top = 50f,
                        bottom = 60f,
                        marginLeft = 70f,
                        marginRight = 80f,
                        marginTop = 90f,
                        marginBottom = 100f,
                        paddingLeft = 110f,
                        paddingRight = 120f,
                        paddingTop = 130f,
                        paddingBottom = 140f
                ))
    }

    @Test
    fun givenMinimalAbsoluteDimensionsItShouldCalculateValues() {
        view.apply {
            marginLeft = 70.pixels()
            marginRight = 80.pixels()
            marginTop = 90.pixels()
            marginBottom = 100.pixels()
            paddingLeft = 110.pixels()
            paddingRight = 120.pixels()
            paddingTop = 130.pixels()
            paddingBottom = 140.pixels()
        }

        val dimensions = absoluteViewDimensionsCalculator.calculate(view)

        assertThat(dimensions)
                .isEqualTo(AbsoluteViewDimensions(
                        width = null,
                        height = null,
                        left = null,
                        right = null,
                        top = null,
                        bottom = null,
                        marginLeft = 70f,
                        marginRight = 80f,
                        marginTop = 90f,
                        marginBottom = 100f,
                        paddingLeft = 110f,
                        paddingRight = 120f,
                        paddingTop = 130f,
                        paddingBottom = 140f
                ))
    }

    @Test
    fun givenRelativeDimensionsItShouldCalculateValues() {
        every { view.parentArea } returns rectangleOf(50f, 250f, 100f, 500f)
        view.apply {
            width = 5.percent()
            height = 10.percent()
            left = 15.percent()
            right = 20.percent()
            top = 25.percent()
            bottom = 30.percent()
            marginLeft = 35.percent()
            marginRight = 40.percent()
            marginTop = 45.percent()
            marginBottom = 50.percent()
            paddingLeft = 55.percent()
            paddingRight = 60.percent()
            paddingTop = 65.percent()
            paddingBottom = 70.percent()
        }

        val dimensions = absoluteViewDimensionsCalculator.calculate(view)

        assertThat(dimensions)
                .isEqualTo(AbsoluteViewDimensions(
                        width = 10f,
                        height = 40f,
                        left = 30f,
                        right = 40f,
                        top = 100f,
                        bottom = 120f,
                        marginLeft = 70f,
                        marginRight = 80f,
                        marginTop = 180f,
                        marginBottom = 200f,
                        paddingLeft = 110f,
                        paddingRight = 120f,
                        paddingTop = 260f,
                        paddingBottom = 280f
                ))
    }

    @Test
    fun givenMinimalRelativeDimensionsItShouldCalculateValues() {
        every { view.parentArea } returns rectangleOf(50f, 250f, 100f, 500f)
        view.apply {
            marginLeft = 35.percent()
            marginRight = 40.percent()
            marginTop = 45.percent()
            marginBottom = 50.percent()
            paddingLeft = 55.percent()
            paddingRight = 60.percent()
            paddingTop = 65.percent()
            paddingBottom = 70.percent()
        }

        val dimensions = absoluteViewDimensionsCalculator.calculate(view)

        assertThat(dimensions)
                .isEqualTo(AbsoluteViewDimensions(
                        width = null,
                        height = null,
                        left = null,
                        right = null,
                        top = null,
                        bottom = null,
                        marginLeft = 70f,
                        marginRight = 80f,
                        marginTop = 180f,
                        marginBottom = 200f,
                        paddingLeft = 110f,
                        paddingRight = 120f,
                        paddingTop = 260f,
                        paddingBottom = 280f
                ))
    }

}