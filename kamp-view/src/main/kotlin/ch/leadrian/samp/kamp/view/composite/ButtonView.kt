package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.BackgroundView
import ch.leadrian.samp.kamp.view.base.ClickableView
import ch.leadrian.samp.kamp.view.base.TextTransformer
import ch.leadrian.samp.kamp.view.base.TextView
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import ch.leadrian.samp.kamp.view.style.ButtonStyle
import ch.leadrian.samp.kamp.view.style.Style
import java.util.*

open class ButtonView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory
) : ClickableView(player, viewContext) {

    private lateinit var backgroundView: BackgroundView

    private lateinit var textView: TextView

    var textPadding: ViewDimension = 0.pixels()
        set(value) {
            field = value
            backgroundView.setPadding(value)
        }

    private val backgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.GREY)

    var backgroundColor: Color by backgroundColorSupplier

    fun backgroundColor(backgroundColorSupplier: () -> Color) {
        this.backgroundColorSupplier.value(backgroundColorSupplier)
    }

    private val disabledBackgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.LIGHT_GRAY)

    var disabledBackgroundColor: Color by disabledBackgroundColorSupplier

    fun disabledBackgroundColor(disabledBackgroundColorSupplier: () -> Color) {
        this.disabledBackgroundColorSupplier.value(disabledBackgroundColorSupplier)
    }

    private val textBackgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.BLACK)

    var textBackgroundColor: Color by textBackgroundColorSupplier

    fun textBackgroundColor(textBackgroundColorSupplier: () -> Color) {
        this.textBackgroundColorSupplier.value(textBackgroundColorSupplier)
    }

    private val disabledTextBackgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.DARK_GRAY)

    var disabledTextBackgroundColor: Color by disabledTextBackgroundColorSupplier

    fun disabledTextBackgroundColor(disabledTextBackgroundColorSupplier: () -> Color) {
        this.disabledTextBackgroundColorSupplier.value(disabledTextBackgroundColorSupplier)
    }

    private val textColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.BLACK)

    var textColor: Color by textColorSupplier

    fun textColor(textColorSupplier: () -> Color) {
        this.textColorSupplier.value(textColorSupplier)
    }

    private val disabledTextColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.DARK_GRAY)

    var disabledTextColor: Color by disabledTextColorSupplier

    fun disabledTextColor(disabledTextColorSupplier: () -> Color) {
        this.disabledTextColorSupplier.value(disabledTextColorSupplier)
    }

    var font: TextDrawFont
        get() = textView.font
        set(value) {
            textView.font = value
        }

    var outlineSize: Int
        get() = textView.outlineSize
        set(value) {
            textView.outlineSize = value
        }

    var shadowSize: Int
        get() = textView.shadowSize
        set(value) {
            textView.shadowSize = value
        }

    var alignment: TextDrawAlignment
        get() = textView.alignment
        set(value) {
            textView.alignment = value
        }

    var text: String
        get() = textView.text
        set(value) {
            textView.text = value
        }

    var textTransformer: TextTransformer?
        get() = textView.textTransformer
        set(value) {
            textView.textTransformer = value
        }

    fun setText(text: String, vararg args: Any) {
        this.textView.setText(text, *args)
    }

    fun setText(textKey: TextKey) {
        this.textView.setText(textKey)
    }

    fun setText(textKey: TextKey, vararg args: Any) {
        this.textView.setText(textKey, *args)
    }

    fun text(textSupplier: (Locale) -> String) {
        this.textView.text(textSupplier)
    }

    override fun applyStyle(style: Style): Boolean {
        super.applyStyle(style)
        if (style is ButtonStyle) {
            textPadding = style.buttonTextPadding
            backgroundColor = style.buttonBackgroundColor
            disabledBackgroundColor = style.disabledButtonBackgroundColor
            textColor = style.buttonTextColor
            disabledTextColor = style.disabledButtonTextColor
            textBackgroundColor = style.buttonTextBackgroundColor
            disabledTextBackgroundColor = style.disabledButtonTextBackgroundColor
            font = style.buttonTextFont
            outlineSize = style.buttonTextOutlineSize
            shadowSize = style.buttonTextShadowSize
            alignment = style.buttonTextAlignment
            textTransformer = style.buttonTextTransformer
        }
        return false
    }

    init {
        with(viewFactory) {
            backgroundView = this@ButtonView.backgroundView {
                color {
                    if (this@ButtonView.isEnabled) {
                        this@ButtonView.backgroundColor
                    } else {
                        this@ButtonView.disabledBackgroundColor
                    }
                }
                setPadding(textPadding)
                onClick { this@ButtonView.click() }
                textView = textView {
                    color {
                        if (this@ButtonView.isEnabled) {
                            this@ButtonView.textColor
                        } else {
                            this@ButtonView.disabledTextColor
                        }
                    }
                    backgroundColor {
                        if (this@ButtonView.isEnabled) {
                            this@ButtonView.textBackgroundColor
                        } else {
                            this@ButtonView.disabledTextBackgroundColor
                        }
                    }
                    alignment = TextDrawAlignment.CENTERED
                    font = TextDrawFont.FONT2
                    outlineSize = 0
                    shadowSize = 0
                    letterHeight = 100.percent()
                    onClick { this@ButtonView.click() }
                }
            }
        }
        enable()
    }

    override fun onEnable() {
        super.onEnable()
        backgroundView.enable()
        textView.enable()
    }

    override fun onDisable() {
        super.onDisable()
        backgroundView.disable()
        textView.disable()
    }

}