package ch.leadrian.samp.kamp.view.layout

fun pixelsToLetterSize(pixels: Float): Float = (pixels - 3.6f) / 9.0f

fun letterSizeToPixels(letterSize: Float): Float = 9.0f * letterSize + 3.6f
