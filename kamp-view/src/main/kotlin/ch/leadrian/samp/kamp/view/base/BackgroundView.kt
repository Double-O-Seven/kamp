package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext

open class BackgroundView(
        player: Player,
        viewContext: ViewContext,
        private val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private val colorSupplier: ValueSupplier<Color> = ValueSupplier(colorOf(0x00000080))

    var color: Color by colorSupplier

    fun color(colorSupplier: () -> Color) {
        this.colorSupplier.value(colorSupplier)
    }

    override fun onDraw() {
        if (isInvalidated) {
            replaceTextDraw(paddingArea)
        } else {
            updateTextDraw()
        }
    }

    private fun replaceTextDraw(area: Rectangle) {
        textDraw?.destroy()

        textDraw = playerTextDrawService.createPlayerTextDraw(
                player,
                TextDrawCodes.EMPTY_TEXT,
                vector2DOf(
                        x = screenMinXToTextDrawMinX(area.minX),
                        y = screenMinYToTextDrawMinY(area.minY)
                )
        ).also {
            it.alignment = TextDrawAlignment.LEFT
            it.textSize = vector2DOf(
                    x = screenMinXAndWidthToTextDrawMaxX(minX = area.minX, width = area.width),
                    y = screenHeightToTextDrawHeightByLetterSize(area.height)
            )
            it.letterSize = vector2DOf(x = 0f, y = screenHeightToLetterSizeY(area.height))
            it.useBox = true
            it.boxColor = color
            it.isSelectable = isEnabled
            it.onClick { click() }
            it.show()
        }
    }

    private fun updateTextDraw() {
        textDraw?.also {
            var show = false

            if (it.boxColor != color) {
                it.boxColor = color
                show = true
            }

            if (it.isSelectable != isEnabled) {
                it.isSelectable = isEnabled
                show = true
            }

            if (show) {
                it.show()
            }
        }
    }

    override fun onHide() {
        destroyTextDraw()
    }

    override fun destroyContent() {
        destroyTextDraw()
    }

    private fun destroyTextDraw() {
        textDraw?.destroy()
        textDraw = null
    }
}