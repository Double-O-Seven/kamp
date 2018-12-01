package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*

open class TextView(
        player: Player,
        layoutCalculator: ViewLayoutCalculator,
        protected val textProvider: TextProvider,
        protected val textFormatter: TextFormatter,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, layoutCalculator) {

    private var textDraw: PlayerTextDraw? = null

    var font: TextDrawFont = TextDrawFont.FONT2

    var outlineSize: Int = 1

    var shadowSize: Int = 0

    var isProportional: Boolean = true

    var alignment: TextDrawAlignment = TextDrawAlignment.LEFT

    var letterHeight: ViewDimension = 32.pixels()

    var letterWidth: ViewDimension = pixels { letterHeight.getValue(layout.height) / font.idealHeightToWidthRatio }

    var color: Color = Colors.WHITE

    var backgroundColor: Color = Colors.TRANSPARENT

    private var textSupplier: (Locale) -> String = { "_" }

    var text: String
        set(value) {
            textSupplier = { value }
        }
        get() {
            val text = textSupplier(player.locale)
            return when {
                text.isBlank() -> TextDrawCodes.EMPTY_TEXT
                else -> text
            }
        }

    fun setText(text: String, vararg args: Any) {
        textSupplier = { locale -> textFormatter.format(locale, text, args) }
    }

    fun setText(textKey: TextKey) {
        textSupplier = { locale -> textProvider.getText(locale, textKey) }
    }

    fun setText(textKey: TextKey, vararg args: Any) {
        textSupplier = { locale ->
            val text = textProvider.getText(locale, textKey)
            textFormatter.format(locale, text, args)
        }
    }

    fun text(textSupplier: (Locale) -> String) {
        this.textSupplier = textSupplier
    }

    override fun draw(layout: Rectangle) {
        if (textDraw == null || isInvalidated) {
            replaceTextDraw(layout)
        } else {
            updateTextDraw()
        }
    }

    private fun replaceTextDraw(layout: Rectangle) {
        textDraw?.destroy()
        textDraw = createTextDraw(layout).apply {
            outlineSize = this@TextView.outlineSize
            shadowSize = this@TextView.shadowSize
            isProportional = this@TextView.isProportional
            isSelectable = isEnabled
            font = this@TextView.font
            letterSize = vector2DOf(
                    x = pixelsToLetterSize(this@TextView.letterWidth.getValue(layout.width)),
                    y = pixelsToLetterSize(this@TextView.letterHeight.getValue(layout.height))
            )
            alignment = this@TextView.alignment
            backgroundColor = this@TextView.backgroundColor
            color = this@TextView.color
            onClick { click() }
            show()
        }
    }

    private fun pixelsToLetterSize(pixels: Float): Float = (pixels - 3.6f) / 9.0f

    private fun updateTextDraw() {
        textDraw?.apply {
            var show = false

            if (color != this@TextView.color) {
                color = this@TextView.color
                show = true
            }

            if (backgroundColor != this@TextView.backgroundColor) {
                backgroundColor = this@TextView.backgroundColor
                show = true
            }

            if (isSelectable != this@TextView.isEnabled) {
                isSelectable = this@TextView.isEnabled
                show = true
            }

            // Text is supplied, don't get it twice
            this@TextView.text.takeIf { it != text }?.let {
                text = it
                // No need to show again, text draw will update automatically
            }

            if (show) {
                show()
            }
        }
    }

    private fun createTextDraw(layout: Rectangle): PlayerTextDraw {
        return when (alignment) {
            TextDrawAlignment.CENTERED -> createCenteredTextDraw(layout)
            TextDrawAlignment.LEFT -> createLeftAlignedTextDraw(layout)
            TextDrawAlignment.RIGHT -> createRightAlignedTextDraw(layout)
        }
    }

    private fun createCenteredTextDraw(layout: Rectangle): PlayerTextDraw {
        val position = vector2DOf(
                x = layout.minX + (TEXT_DRAW_OFFSET_LEFT_SIDE + layout.width) / 2f,
                y = layout.minY
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            // This weird thing is correct, x and y are switched
            textSize = vector2DOf(x = layout.height, y = layout.width)
        }
    }

    private fun createLeftAlignedTextDraw(layout: Rectangle): PlayerTextDraw {
        val position = vector2DOf(
                x = layout.minX + TEXT_DRAW_OFFSET_LEFT_SIDE,
                y = layout.minY
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            textSize = vector2DOf(x = layout.minX + layout.width, y = layout.height)
        }
    }

    private fun createRightAlignedTextDraw(layout: Rectangle): PlayerTextDraw {
        val position = vector2DOf(
                x = layout.minX + layout.width,
                y = layout.minY
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            textSize = vector2DOf(x = layout.minX, y = layout.height)
        }
    }

    override fun onHide() {
        destroyTextDraw()
    }

    override fun onDestroy() {
        destroyTextDraw()
    }

    private fun destroyTextDraw() {
        textDraw?.destroy()
        textDraw = null
    }

}

val TextDrawFont.idealHeightToWidthRatio: Float
    get() = when (this) {
        TextDrawFont.BANK_GOTHIC, TextDrawFont.FONT2 -> 5.5f
        TextDrawFont.DIPLOMA, TextDrawFont.PRICEDOWN -> 4.0f
        else -> 1f
    }
