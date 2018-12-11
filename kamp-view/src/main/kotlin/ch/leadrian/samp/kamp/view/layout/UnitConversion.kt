package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.screenresolution.screenResolution

private val MAGIC_NUMBER: Float = Math.pow(4.0 / 3.0, 1.0 / 4.0).toFloat()

fun pixelsToLetterSize(pixels: Float): Float = (pixels - 3.6f) / 9.0f

fun letterSizeToPixels(letterSize: Float): Float = 9.0f * letterSize + 3.6f

fun screenYCoordinateToTextDrawBoxY(y: Float): Float = (y + 2.2222f) / MAGIC_NUMBER

fun screenHeightToTextDrawBoxHeight(height: Float): Float = (height + 4.4444f) / 10f

fun Player.screenYCoordinateToTextDrawBoxY(y: Float): Float = (y + verticalTextDrawBoxOffset) / MAGIC_NUMBER

fun Player.screenHeightToTextDrawBoxHeight(height: Float): Float = height / 10f

val Player.verticalTextDrawBoxOffset: Float
    get() = 4f * 480f / this.screenResolution.height.toFloat()

val Player.horizontalTextDrawBoxOffset: Float
    get() = 4f * 640f / this.screenResolution.width

val TextDrawFont.optimalHeightToWidthRatio: Float
    get() = when (this) {
        TextDrawFont.BANK_GOTHIC, TextDrawFont.FONT2 -> 5.5f
        TextDrawFont.DIPLOMA, TextDrawFont.PRICEDOWN -> 4.0f
        else -> 1f
    }
