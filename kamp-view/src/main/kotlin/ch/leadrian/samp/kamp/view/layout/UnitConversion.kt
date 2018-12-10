package ch.leadrian.samp.kamp.view.layout

import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont

fun pixelsToLetterSize(pixels: Float): Float = (pixels - 3.6f) / 9.0f

fun letterSizeToPixels(letterSize: Float): Float = 9.0f * letterSize + 3.6f

fun screenYCoordinateToTextDrawBoxY(y: Float): Float = (y + 2.0483f) / 1.0724f

fun screenHeightToTextDrawBoxHeight(height: Float): Float = (height - 4.3373f) / 9.6386f

val TextDrawFont.optimalHeightToWidthRatio: Float
    get() = when (this) {
        TextDrawFont.BANK_GOTHIC, TextDrawFont.FONT2 -> 5.5f
        TextDrawFont.DIPLOMA, TextDrawFont.PRICEDOWN -> 4.0f
        else -> 1f
    }
