package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.view.base.TextTransformer
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.pixels

interface ButtonStyle : Style {

    @JvmDefault
    val buttonTextPadding: ViewDimension
        get() = 0.pixels()

    @JvmDefault
    val buttonBackgroundColor: Color
        get() = Colors.GREY

    @JvmDefault
    val disabledButtonBackgroundColor: Color
        get() = Colors.LIGHT_GRAY

    @JvmDefault
    val buttonTextColor: Color
        get() = Colors.BLACK

    @JvmDefault
    val disabledButtonTextColor: Color
        get() = Colors.DARK_GRAY

    @JvmDefault
    val buttonTextBackgroundColor: Color
        get() = Colors.BLACK

    @JvmDefault
    val disabledButtonTextBackgroundColor: Color
        get() = Colors.DARK_GRAY

    @JvmDefault
    val buttonTextFont: TextDrawFont
        get() = TextDrawFont.FONT2

    @JvmDefault
    val buttonTextOutlineSize: Int
        get() = 1

    @JvmDefault
    val buttonTextShadowSize: Int
        get() = 0

    @JvmDefault
    val buttonTextAlignment: TextDrawAlignment
        get() = TextDrawAlignment.LEFT

    @JvmDefault
    val buttonTextTransformer: TextTransformer?
        get() = null
}