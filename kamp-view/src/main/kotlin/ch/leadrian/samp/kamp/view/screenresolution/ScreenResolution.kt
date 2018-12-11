package ch.leadrian.samp.kamp.view.screenresolution

data class ScreenResolution(val width: Int, val height: Int)

infix fun Int.x(height: Int) = ScreenResolution(width = this, height = height)
