package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.view.base.TextTransformer
import ch.leadrian.samp.kamp.view.base.TextTransformers
import ch.leadrian.samp.kamp.view.layout.ViewDimension

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class MaterialDesignStyle(
        val colorPrimary: Color,
        val colorPrimaryDark: Color,
        val colorPrimaryLight: Color,
        val colorSecondary: Color,
        val colorSecondaryDark: Color,
        val colorSecondaryLight: Color,
        val textColorPrimary: Color,
        val textColorSecondary: Color,
        val themeColor: Color,
        val themeTextColor: Color,
        val colorError: Color
) : ButtonStyle, DialogStyle, ProgressBarStyle, ScrollBarStyle, TextInputStyle {

    override val buttonTextPadding: ViewDimension
        get() = smallPadding

    override val buttonBackgroundColor: Color
        get() = colorSecondary

    override val disabledButtonBackgroundColor: Color
        get() = colorSecondaryLight

    override val buttonTextColor: Color
        get() = textColorSecondary

    override val disabledButtonTextColor: Color
        get() = textColorSecondary

    override val buttonTextBackgroundColor: Color
        get() = textColorSecondary

    override val disabledButtonTextBackgroundColor: Color
        get() = textColorSecondary

    override val buttonTextFont: TextDrawFont = TextDrawFont.FONT2

    override val buttonTextOutlineSize: Int = 0

    override val buttonTextShadowSize: Int = 0

    override val buttonTextAlignment: TextDrawAlignment = TextDrawAlignment.CENTERED

    override val buttonTextTransformer: TextTransformer = TextTransformers.toUpperCase()

    override val dialogTitleBarPadding: ViewDimension
        get() = smallPadding

    override val dialogTitleBarColor: Color
        get() = colorPrimary

    override val dialogTitleColor: Color
        get() = textColorPrimary

    override val dialogTitleBackgroundColor: Color
        get() = textColorPrimary

    override val dialogContentBackgroundColor: Color
        get() = themeColor

    override val dialogTitleOutlineSize: Int = 0

    override val dialogTitleShadowSize: Int = 0

    override val dialogTitleFont: TextDrawFont = TextDrawFont.BANK_GOTHIC

    override val progressBarPrimaryColor: Color
        get() = colorSecondary

    override val progressBarSecondaryColor: Color
        get() = colorSecondaryDark

    override val progressBarOutlineColor: Color
        get() = themeTextColor

    override val scrollBarColor: Color
        get() = colorPrimary

    override val scrollBarBackgroundColor: Color
        get() = colorPrimaryLight

    override val scrollBarButtonColor: Color
        get() = textColorPrimary

    override val textInputErrorColor: Color
        get() = colorError

    override val textInputFont: TextDrawFont = TextDrawFont.FONT2

    override val textInputOutlineSize: Int = 0

    override val textInputShadowSize: Int = 0

    override val textInputColor: Color
        get() = textColorPrimary

    override val textInputBackgroundColor: Color
        get() = textColorPrimary

    override val disabledTextInputColor: Color
        get() = textColorPrimary

    override val disabledTextInputBackgroundColor: Color
        get() = textColorPrimary

    override val textInputFieldColor: Color
        get() = colorPrimaryLight

    override val textInputTitleFont: TextDrawFont = TextDrawFont.FONT2

    override val textInputTitleOutlineSize: Int = 0

    override val textInputTitleShadowSize: Int = 0

    override val textInputTitleColor: Color
        get() = colorSecondary

    override val textInputTitleBackgroundColor: Color
        get() = colorSecondary

}