package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.view.base.View
import javax.inject.Inject

internal class AbsoluteViewDimensionsCalculator
@Inject
constructor() {

    fun calculate(view: View): AbsoluteViewDimensions {
        return with(view.parentArea) {
            AbsoluteViewDimensions(
                    width = view.width?.getValue(width),
                    height = view.height?.getValue(height),
                    left = view.left?.getValue(width),
                    right = view.right?.getValue(width),
                    top = view.top?.getValue(height),
                    bottom = view.bottom?.getValue(height),
                    marginLeft = view.marginLeft.getValue(width),
                    marginRight = view.marginRight.getValue(width),
                    marginTop = view.marginTop.getValue(height),
                    marginBottom = view.marginBottom.getValue(height),
                    paddingLeft = view.paddingLeft.getValue(width),
                    paddingRight = view.paddingRight.getValue(width),
                    paddingTop = view.paddingTop.getValue(height),
                    paddingBottom = view.paddingBottom.getValue(height)
            )
        }
    }

}