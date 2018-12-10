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
import ch.leadrian.samp.kamp.view.TEXT_DRAW_OFFSET_LEFT
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.layout.screenHeightToTextDrawBoxHeight
import ch.leadrian.samp.kamp.view.layout.screenYCoordinateToTextDrawBoxY

open class BackgroundView(
        player: Player,
        viewContext: ViewContext,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private var colorSupplier: () -> Color = { colorOf(0x00000080) }

    var color: Color
        get() = colorSupplier()
        set(value) {
            val color = value.toColor()
            colorSupplier = { color }
        }

    fun color(colorSupplier: () -> Color) {
        this.colorSupplier = colorSupplier
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
                vector2DOf(x = area.minX, y = screenYCoordinateToTextDrawBoxY(area.minY))
        ).also {
            it.alignment = TextDrawAlignment.LEFT
            it.textSize = vector2DOf(
                    x = area.minX + area.width - TEXT_DRAW_OFFSET_LEFT / 2f,
                    y = screenHeightToTextDrawBoxHeight(area.height) / 0.135f
            )
            it.letterSize = vector2DOf(x = 0f, y = screenHeightToTextDrawBoxHeight(area.height))
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

    override fun onDestroy() {
        destroyTextDraw()
    }

    private fun destroyTextDraw() {
        textDraw?.destroy()
        textDraw = null
    }
}