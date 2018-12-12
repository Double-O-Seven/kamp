package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.pixels

interface Style {

    @JvmDefault
    val hoverColor: Color
        get() = Colors.CYAN

    @JvmDefault
    val tinyMargin: ViewDimension
        get() = 2.pixels()

    @JvmDefault
    val smallMargin: ViewDimension
        get() = 4.pixels()

    @JvmDefault
    val mediumMargin: ViewDimension
        get() = 8.pixels()

    @JvmDefault
    val largeMargin: ViewDimension
        get() = 16.pixels()

    @JvmDefault
    val hugeMargin: ViewDimension
        get() = 32.pixels()

}