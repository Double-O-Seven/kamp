package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import javax.inject.Inject

internal class ViewAreaCalculator
@Inject
constructor() {

    fun calculateContentArea(parentArea: Rectangle, absoluteViewDimensions: AbsoluteViewDimensions): Rectangle {
        with(absoluteViewDimensions) {
            val minX: Float
            val maxX: Float
            when {
                width != null -> {
                    when {
                        left != null || right == null -> {
                            minX = parentArea.minX + (left ?: 0f) + marginLeft + paddingLeft
                            maxX = minX + width
                        }
                        else -> {
                            maxX = parentArea.maxX - right - marginRight - paddingRight
                            minX = maxX - width
                        }
                    }
                }
                else -> {
                    minX = parentArea.minX + (left ?: 0f) + marginLeft + paddingLeft
                    maxX = parentArea.maxX - (right ?: 0f) - marginRight - paddingRight
                }
            }
            val minY: Float
            val maxY: Float
            when {
                height != null -> {
                    when {
                        top != null || bottom == null -> {
                            minY = parentArea.minY + (top ?: 0f) + marginTop + paddingTop
                            maxY = minY + height
                        }
                        else -> {
                            maxY = parentArea.maxY - bottom - marginBottom - paddingBottom
                            minY = maxY - height
                        }
                    }
                }
                else -> {
                    minY = parentArea.minY + (top ?: 0f) + marginTop + paddingTop
                    maxY = parentArea.maxY - (bottom ?: 0f) - marginBottom - paddingBottom
                }
            }
            return rectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY)
        }
    }

    fun calculatePaddingArea(contentArea: Rectangle, absoluteViewDimensions: AbsoluteViewDimensions): Rectangle {
        with(absoluteViewDimensions) {
            val minX = contentArea.minX - paddingLeft
            val maxX = contentArea.maxX + paddingRight
            val minY = contentArea.minY - paddingTop
            val maxY = contentArea.maxY + paddingBottom
            return rectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY)
        }
    }

    fun calculateMarginArea(paddingArea: Rectangle, absoluteViewDimensions: AbsoluteViewDimensions): Rectangle {
        with(absoluteViewDimensions) {
            val minX = paddingArea.minX - marginLeft
            val maxX = paddingArea.maxX + marginRight
            val minY = paddingArea.minY - marginTop
            val maxY = paddingArea.maxY + marginBottom
            return rectangleOf(minX = minX, maxX = maxX, minY = minY, maxY = maxY)
        }
    }

}