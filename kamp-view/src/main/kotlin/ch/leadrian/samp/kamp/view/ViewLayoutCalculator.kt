package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.MutableRectangle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.mutableRectangleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import javax.inject.Inject

class ViewLayoutCalculator
@Inject
constructor() {

    companion object {

        val SCREEN_LAYOUT = rectangleOf(minX = 0f, maxX = 640f, minY = 0f, maxY = 480f)

    }

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
            else -> parentLayout.width - marginLeft - marginRight
        }
    }

    private fun calculateHeight(dimensionValues: ViewDimensionValues): Float = with(dimensionValues) {
        when {
            height != null -> height
            else -> parentLayout.height - marginTop - marginBottom
        }
    }

    private fun calculateX(width: Float, dimensionValues: ViewDimensionValues): Float {
        return with(dimensionValues) {
            val relativeX = when {
                left != null -> left + marginLeft
                right != null -> parentLayout.width - right - width - marginRight
                else -> marginLeft
            }
            relativeX + parentLayout.minX
        }
    }

    private fun calculateY(height: Float, dimensionValues: ViewDimensionValues): Float {
        return with(dimensionValues) {
            val relativeY = when {
                top != null -> top + marginTop
                bottom != null -> parentLayout.height - bottom - height - marginBottom
                else -> marginTop
            }
            relativeY + parentLayout.minY
        }
    }

    private fun calculateDimensionValues(view: View): ViewDimensionValues {
        val parentOfParentLayout: Rectangle = view.parent?.parent?.layout ?: SCREEN_LAYOUT
        val paddingLeft = view.parent?.paddingLeft?.getValue(parentOfParentLayout.width) ?: 0f
        val paddingRight = view.parent?.paddingRight?.getValue(parentOfParentLayout.width) ?: 0f
        val paddingTop = view.parent?.paddingTop?.getValue(parentOfParentLayout.height) ?: 0f
        val paddingBottom = view.parent?.paddingBottom?.getValue(parentOfParentLayout.height) ?: 0f
        val parentLayout = (view.parent?.layout ?: SCREEN_LAYOUT).toMutableRectangle().apply {
            minX += paddingLeft
            maxX -= paddingRight
            minY += paddingTop
            maxY -= paddingBottom
        }
        return ViewDimensionValues(
                parentLayout = parentLayout,
                width = view.width?.getValue(parentLayout.width),
                height = view.height?.getValue(parentLayout.height),
                left = view.left?.getValue(parentLayout.width),
                right = view.right?.getValue(parentLayout.width),
                top = view.top?.getValue(parentLayout.height),
                bottom = view.bottom?.getValue(parentLayout.height),
                marginLeft = view.marginLeft.getValue(parentLayout.width),
                marginRight = view.marginRight.getValue(parentLayout.width),
                marginTop = view.marginTop.getValue(parentLayout.height),
                marginBottom = view.marginBottom.getValue(parentLayout.height)
        )
    }

    private class ViewDimensionValues(
            val parentLayout: Rectangle,
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