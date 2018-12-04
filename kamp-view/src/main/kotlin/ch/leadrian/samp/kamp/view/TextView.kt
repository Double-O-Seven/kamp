package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.util.MAX_TEXT_DRAW_STRING_LENGTH
import java.util.*

open class TextView(
        player: Player,
        viewContext: ViewContext,
        protected val textProvider: TextProvider,
        protected val textFormatter: TextFormatter,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    var font: TextDrawFont = TextDrawFont.FONT2

    var outlineSize: Int = 1

    var shadowSize: Int = 0

    var isProportional: Boolean = true

    var alignment: TextDrawAlignment = TextDrawAlignment.LEFT

    var letterHeight: ViewDimension = 32.pixels()

    var letterWidth: ViewDimension = pixels { letterHeight.getValue(contentArea.height) / font.idealHeightToWidthRatio }

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
                text.length > MAX_TEXT_DRAW_STRING_LENGTH -> text.substring(0, MAX_TEXT_DRAW_STRING_LENGTH)
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

    override fun onDraw() {
        if (textDraw == null || isInvalidated) {
            replaceTextDraw()
        } else {
            updateTextDraw()
        }
    }

    private fun replaceTextDraw() {
        textDraw?.destroy()
        textDraw = createTextDraw().apply {
            outlineSize = this@TextView.outlineSize
            shadowSize = this@TextView.shadowSize
            isProportional = this@TextView.isProportional
            isSelectable = isEnabled
            font = this@TextView.font
            letterSize = vector2DOf(
                    x = pixelsToLetterSize(this@TextView.letterWidth.getValue(contentArea.width)),
                    y = pixelsToLetterSize(this@TextView.letterHeight.getValue(contentArea.height))
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
                // No need to show again, text onDraw will update automatically
            }

            if (show) {
                show()
            }
        }
    }

    private fun createTextDraw(): PlayerTextDraw {
        return when (alignment) {
            TextDrawAlignment.CENTERED -> createCenteredTextDraw()
            TextDrawAlignment.LEFT -> createLeftAlignedTextDraw()
            TextDrawAlignment.RIGHT -> createRightAlignedTextDraw()
        }
    }

    private fun createCenteredTextDraw(): PlayerTextDraw {
        val position = vector2DOf(
                x = contentArea.minX + (TEXT_DRAW_OFFSET_LEFT_SIDE + contentArea.width) / 2f,
                y = contentArea.minY
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            // This weird thing is correct, x and y are switched
            textSize = vector2DOf(x = contentArea.height, y = contentArea.width)
        }
    }

    private fun createLeftAlignedTextDraw(): PlayerTextDraw {
        val position = vector2DOf(
                x = contentArea.minX + TEXT_DRAW_OFFSET_LEFT_SIDE,
                y = contentArea.minY
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            textSize = vector2DOf(x = contentArea.minX + contentArea.width, y = contentArea.height)
        }
    }

    private fun createRightAlignedTextDraw(): PlayerTextDraw {
        val position = vector2DOf(
                x = contentArea.minX + contentArea.width,
                y = contentArea.minY
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            textSize = vector2DOf(x = contentArea.minX, y = contentArea.height)
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
