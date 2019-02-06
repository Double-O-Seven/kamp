package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors

interface TextInputStyle : Style {

    @JvmDefault
    val invalidInputColor: Color
        get() = Colors.RED

    @JvmDefault
    val inputTextFont: TextDrawFont
        get() = TextDrawFont.FONT2

    val inputTextOutlineSize: Int
        get() = 1

    val inputTextShadowSize: Int
        get() = 0

    @JvmDefault
    val inputTextColor: Color
        get() = Colors.DARK_GRAY

    @JvmDefault
    val inputTextBackgroundColor: Color
        get() = Colors.LIGHT_GRAY

    @JvmDefault
    val disabledInputTextColor: Color
        get() = Colors.GREY

    @JvmDefault
    val disabledInputTextBackgroundColor: Color
        get() = Colors.GREY

    @JvmDefault
    val inputBackgroundColor: Color
        get() = Colors.LIGHT_GRAY

    @JvmDefault
    val titleFont: TextDrawFont
        get() = TextDrawFont.FONT2

    @JvmDefault
    val titleOutlineSize: Int
        get() = 0

    @JvmDefault
    val titleShadowSize: Int
        get() = 0

    @JvmDefault
    val titleColor: Color
        get() = Colors.BLACK

    @JvmDefault
    val titleBackgroundColor: Color
        get() = Colors.BLACK

}