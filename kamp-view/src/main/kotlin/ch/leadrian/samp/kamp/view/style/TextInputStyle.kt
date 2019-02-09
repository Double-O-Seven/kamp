package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors

interface TextInputStyle : Style {

    @JvmDefault
    val textInputErrorColor: Color
        get() = Colors.RED

    @JvmDefault
    val textInputFont: TextDrawFont
        get() = TextDrawFont.FONT2

    val textInputOutlineSize: Int
        get() = 1

    val textInputShadowSize: Int
        get() = 0

    @JvmDefault
    val textInputColor: Color
        get() = Colors.DARK_GRAY

    @JvmDefault
    val textInputBackgroundColor: Color
        get() = Colors.LIGHT_GRAY

    @JvmDefault
    val disabledTextInputColor: Color
        get() = Colors.GREY

    @JvmDefault
    val disabledTextInputBackgroundColor: Color
        get() = Colors.GREY

    @JvmDefault
    val textInputFieldColor: Color
        get() = Colors.LIGHT_GRAY

    @JvmDefault
    val textInputTitleFont: TextDrawFont
        get() = TextDrawFont.FONT2

    @JvmDefault
    val textInputTitleOutlineSize: Int
        get() = 0

    @JvmDefault
    val textInputTitleShadowSize: Int
        get() = 0

    @JvmDefault
    val textInputTitleColor: Color
        get() = Colors.BLACK

    @JvmDefault
    val textInputTitleBackgroundColor: Color
        get() = Colors.BLACK

}