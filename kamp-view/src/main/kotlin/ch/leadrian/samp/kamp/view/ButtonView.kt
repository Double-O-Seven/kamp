package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import java.util.*

open class ButtonView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory
) : ClickableView(player, viewContext) {

    private lateinit var backgroundView: BackgroundView

    private lateinit var textView: TextView

    var textPadding: ViewDimension = 16.pixels()
        set(value) {
            field = value
            backgroundView.setPadding(value)
        }

    var backgroundColor: Color = Colors.GREY

    var disabledBackgroundColor: Color = Colors.LIGHT_GRAY

    var textColor: Color = Colors.BLACK

    var disabledTextColor: Color = Colors.DARK_GRAY

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

    var isProportional: Boolean
        get() = textView.isProportional
        set(value) {
            textView.isProportional = value
        }

    var alignment: TextDrawAlignment
        get() = textView.alignment
        set(value) {
            textView.alignment = value
        }

    var letterHeight: ViewDimension
        get() = textView.letterHeight
        set(value) {
            textView.letterHeight = value
        }

    var letterWidth: ViewDimension
        get() = textView.letterWidth
        set(value) {
            textView.letterWidth = value
        }

    var text: String
        get() = textView.text
        set(value) {
            textView.text = value
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
                    alignment = TextDrawAlignment.CENTERED
                    color {
                        if (this@ButtonView.isEnabled) {
                            this@ButtonView.textColor
                        } else {
                            this@ButtonView.disabledTextColor
                        }
                    }
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