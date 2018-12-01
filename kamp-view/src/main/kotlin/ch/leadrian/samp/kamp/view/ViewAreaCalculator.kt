package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.MutableRectangle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.mutableRectangleOf
import javax.inject.Inject

class ViewAreaCalculator
@Inject
constructor() {

    fun calculate(view: View): MutableRectangle {
        val dimensions = calculateDimensionValues(view)
        val width = calculateWidth(dimensions)
        val height = calculateHeight(dimensions)
        val x = calculateX(width, dimensions)
        val y = calculateY(height, dimensions)
        return mutableRectangleOf(
                minX = x,
                maxX = x + width,
                minY = y,
                maxY = y + height
        )
    }

    private fun calculateWidth(dimensionValues: ViewDimensionValues): Float = with(dimensionValues) {
        when {
            width != null -> width
            else -> parentArea.width - marginLeft - marginRight
        }
    }

    private fun calculateHeight(dimensionValues: ViewDimensionValues): Float = with(dimensionValues) {
        when {
            height != null -> height
            else -> parentArea.height - marginTop - marginBottom
        }
    }

    private fun calculateX(width: Float, dimensionValues: ViewDimensionValues): Float {
        return with(dimensionValues) {
            val relativeX = when {
                left != null -> left + marginLeft
                right != null -> parentArea.width - right - width - marginRight
                else -> marginLeft
            }
            relativeX + parentArea.minX
        }
    }

    private fun calculateY(height: Float, dimensionValues: ViewDimensionValues): Float {
        return with(dimensionValues) {
            val relativeY = when {
                top != null -> top + marginTop
                bottom != null -> parentArea.height - bottom - height - marginBottom
                else -> marginTop
            }
            relativeY + parentArea.minY
        }
    }

    private fun calculateDimensionValues(view: View): ViewDimensionValues {
        val parentOfParentArea: Rectangle = view.parent?.parent?.area ?: SCREEN_AREA
        val paddingLeft = view.parent?.paddingLeft?.getValue(parentOfParentArea.width) ?: 0f
        val paddingRight = view.parent?.paddingRight?.getValue(parentOfParentArea.width) ?: 0f
        val paddingTop = view.parent?.paddingTop?.getValue(parentOfParentArea.height) ?: 0f
        val paddingBottom = view.parent?.paddingBottom?.getValue(parentOfParentArea.height) ?: 0f
        val parentArea = (view.parent?.area ?: SCREEN_AREA).toMutableRectangle().apply {
            minX += paddingLeft
            maxX -= paddingRight
            minY += paddingTop
            maxY -= paddingBottom
        }
        return ViewDimensionValues(
                parentArea = parentArea,
                width = view.width?.getValue(parentArea.width),
                height = view.height?.getValue(parentArea.height),
                left = view.left?.getValue(parentArea.width),
                right = view.right?.getValue(parentArea.width),
                top = view.top?.getValue(parentArea.height),
                bottom = view.bottom?.getValue(parentArea.height),
                marginLeft = view.marginLeft.getValue(parentArea.width),
                marginRight = view.marginRight.getValue(parentArea.width),
                marginTop = view.marginTop.getValue(parentArea.height),
                marginBottom = view.marginBottom.getValue(parentArea.height)
        )
    }

    private class ViewDimensionValues(
            val parentArea: Rectangle,
            val width: Float?,
            val height: Float?,
            val left: Float?,
            val right: Float?,
            val top: Float?,
            val bottom: Float?,
            val marginLeft: Float,
            val marginRight: Float,
            val marginTop: Float,
            val marginBottom: Float
    )

}