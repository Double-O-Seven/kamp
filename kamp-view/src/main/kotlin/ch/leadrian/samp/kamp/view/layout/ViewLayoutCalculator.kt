package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.view.base.View
import javax.inject.Inject

internal class ViewLayoutCalculator
@Inject
constructor(
        private val absoluteViewDimensionsCalculator: AbsoluteViewDimensionsCalculator,
        private val viewAreaCalculator: ViewAreaCalculator
) {

    fun calculate(view: View): ViewLayout {
        val absoluteViewDimensions = absoluteViewDimensionsCalculator.calculate(view)
        val contentArea = viewAreaCalculator
                .calculateContentArea(parentArea = view.parentArea, absoluteViewDimensions = absoluteViewDimensions)
        val paddingArea = viewAreaCalculator
                .calculatePaddingArea(contentArea = contentArea, absoluteViewDimensions = absoluteViewDimensions)
        val marginArea = viewAreaCalculator
                .calculateMarginArea(paddingArea = paddingArea, absoluteViewDimensions = absoluteViewDimensions)
        return ViewLayout(
                marginArea = marginArea,
                paddingArea = paddingArea,
                contentArea = contentArea
        )
    }

}