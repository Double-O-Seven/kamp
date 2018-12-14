package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.pixels

interface DialogStyle : Style {

    @JvmDefault
    val dialogTitleBarHeight: ViewDimension
        get() = 24.pixels()

    @JvmDefault
    val dialogTitleBarPadding: ViewDimension
        get() = 2.pixels()

    @JvmDefault
    val dialogTitleBarColor: Color
        get() = Colors.GREY

    @JvmDefault
    val dialogTitleColor: Color
        get() = Colors.BLACK

    @JvmDefault
    val dialogTitleBackgroundColor: Color
        get() = Colors.BLACK

    @JvmDefault
    val dialogContentBackgroundColor: Color
        get() = colorOf(0x00000080)

    @JvmDefault
    val dialogTitleOutlineSize: Int
        get() = 0

    @JvmDefault
    val dialogTitleShadowSize: Int
        get() = 0

    @JvmDefault
    val dialogTitleFont: TextDrawFont
        get() = TextDrawFont.FONT2

}