package ch.leadrian.samp.kamp.view.base

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
import ch.leadrian.samp.kamp.core.api.util.MAX_TEXT_DRAW_STRING_LENGTH
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.pixels
import java.util.*

open class TextView(
        player: Player,
        viewContext: ViewContext,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter,
        private val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private companion object {

        val log = loggerFor<TextView>()

    }

    private var textDraw: PlayerTextDraw? = null

    var font: TextDrawFont = TextDrawFont.FONT2

    var outlineSize: Int = 1

    var shadowSize: Int = 0

    var isProportional: Boolean = true

    var alignment: TextDrawAlignment = TextDrawAlignment.LEFT

    var letterHeight: ViewDimension = 16.pixels()

    var letterWidth: ViewDimension = pixels {
        val letterSize = pixelsToLetterSize(letterHeight.getValue(paddingArea.height)) / font.optimalHeightToWidthRatio
        letterSizeToPixels(letterSize)
    }

    private var colorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.WHITE)

    var color: Color by colorSupplier

    fun color(colorSupplier: () -> Color) {
        this.colorSupplier.value(colorSupplier)
    }

    private var backgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.BLACK)

    var backgroundColor: Color by backgroundColorSupplier

    fun backgroundColor(backgroundColorSupplier: () -> Color) {
        this.backgroundColorSupplier.value(backgroundColorSupplier)
    }

    private var textSupplier: (Locale) -> String = { TextDrawCodes.EMPTY_TEXT }

    var textTransformer: TextTransformer? = null

    var text: String
        set(value) {
            textSupplier = { value }
        }
        get() {
            val text = textSupplier(player.locale).let { textTransformer?.transform(it, player.locale) ?: it }
            return when {
                text.isBlank() -> TextDrawCodes.EMPTY_TEXT
                text.length > MAX_TEXT_DRAW_STRING_LENGTH -> text.substring(0, MAX_TEXT_DRAW_STRING_LENGTH)
                else -> text
            }
        }

    fun setText(text: String, vararg args: Any) {
        textSupplier = { locale -> textFormatter.format(locale, text, *args) }
    }

    fun setText(textKey: TextKey) {
        textSupplier = { locale -> textProvider.getText(locale, textKey) }
    }

    fun setText(textKey: TextKey, vararg args: Any) {
        textSupplier = { locale ->
            val text = textProvider.getText(locale, textKey)
            textFormatter.format(locale, text, *args)
        }
    }

    fun text(textSupplier: (Locale) -> String) {
        this.textSupplier = textSupplier
    }

    override fun onDraw() {
        if (textDraw == null || isInvalidated) {
            replaceTextDraw(paddingArea)
        } else {
            updateTextDraw()
        }
    }

    private fun replaceTextDraw(area: Rectangle) {
        textDraw?.destroy()
        textDraw = createTextDraw(area).also {
            it.outlineSize = outlineSize
            it.shadowSize = shadowSize
            it.isProportional = isProportional
            it.isSelectable = isEnabled
            it.font = font
            it.letterSize = vector2DOf(
                    x = pixelsToLetterSize(letterWidth.getValue(area.width)),
                    y = screenHeightToLetterSizeY(letterHeight.getValue(area.height))
            )
            it.backgroundColor = backgroundColor
            it.color = color
            it.onClick { click() }
            it.show()
        }
    }

    private fun updateTextDraw() {
        textDraw?.also {
            var show = false

            if (it.color != color) {
                it.color = color
                show = true
            }

            if (it.backgroundColor != backgroundColor) {
                it.backgroundColor = backgroundColor
                show = true
            }

            if (it.isSelectable != isEnabled) {
                it.isSelectable = isEnabled
                show = true

                if (it.isSelectable && it.alignment == TextDrawAlignment.RIGHT) {
                    log.warn("Right aligned text draws do not wrap text and do not allow text draw selection")
                }
            }

            // Text is supplied, don't get it twice
            val text = this.text
            if (it.text != text) {
                // No need to show again, text onDraw will setItem automatically
                it.text = text
            }

            if (show) {
                it.show()
            }
        }
    }

    private fun createTextDraw(area: Rectangle): PlayerTextDraw {
        return when (alignment) {
            TextDrawAlignment.CENTERED -> createCenteredTextDraw(area)
            TextDrawAlignment.LEFT -> createLeftAlignedTextDraw(area)
            TextDrawAlignment.RIGHT -> createRightAlignedTextDraw(area)
        }
    }

    private fun createLeftAlignedTextDraw(area: Rectangle): PlayerTextDraw {
        val position = vector2DOf(x = screenMinXToTextDrawMinX(area.minX), y = screenMinYToTextDrawMinY(area.minY))
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            alignment = TextDrawAlignment.LEFT
            textSize = vector2DOf(
                    x = screenMinXAndWidthToTextDrawMaxX(minX = area.minX, width = area.width),
                    y = screenHeightToTextDrawHeightByLetterSize(area.height)
            )
        }
    }

    private fun createCenteredTextDraw(area: Rectangle): PlayerTextDraw {
        val position = vector2DOf(
                x = area.minX + horizontalTextDrawBoxOffset + (area.width - horizontalTextDrawBoxOffset) / 2f,
                y = screenMinYToTextDrawMinY(area.minY)
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            alignment = TextDrawAlignment.CENTERED
            // This weird thing is correct, x and y are switched
            textSize = vector2DOf(
                    x = screenHeightToTextDrawHeightByLetterSize(area.height),
                    y = area.width - horizontalTextDrawBoxOffset
            )
        }
    }

    private fun createRightAlignedTextDraw(area: Rectangle): PlayerTextDraw {
        val position = vector2DOf(
                x = screenMinXAndWidthToTextDrawMaxX(minX = area.minX, width = area.width),
                y = screenMinYToTextDrawMinY(area.minY)
        )
        return playerTextDrawService.createPlayerTextDraw(player, text, position).apply {
            alignment = TextDrawAlignment.RIGHT
            // Doesn't seem to have any effect
            textSize = vector2DOf(
                    x = screenMinYToTextDrawMinY(area.minX),
                    y = screenHeightToTextDrawHeightByLetterSize(area.height)
            )
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

    private fun pixelsToLetterSize(pixels: Float): Float = (pixels - 3.6f) / 9.0f

    private fun letterSizeToPixels(letterSize: Float): Float = 9.0f * letterSize + 3.6f

    private val TextDrawFont.optimalHeightToWidthRatio: Float
        get() = when (this) {
            TextDrawFont.BANK_GOTHIC, TextDrawFont.FONT2 -> 5.5f
            TextDrawFont.DIPLOMA, TextDrawFont.PRICEDOWN -> 4.0f
            else -> 1f
        }

}
