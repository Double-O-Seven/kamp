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
import ch.leadrian.samp.kamp.view.layout.horizontalTextDrawBoxOffset
import ch.leadrian.samp.kamp.view.layout.screenHeightToTextDrawBoxHeight
import ch.leadrian.samp.kamp.view.layout.screenYCoordinateToTextDrawBoxY

open class BackgroundView(
        player: Player,
        viewContext: ViewContext,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private var colorSupplier: ValueSupplier<Color> = ValueSupplier(colorOf(0x00000080))

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
                vector2DOf(x = area.minX + player.horizontalTextDrawBoxOffset, y = player.screenYCoordinateToTextDrawBoxY(area.minY))
        ).also {
            it.alignment = TextDrawAlignment.LEFT
            it.textSize = vector2DOf(x = area.minX + area.width - player.horizontalTextDrawBoxOffset, y = 0f)
            it.letterSize = vector2DOf(x = 0f, y = player.screenHeightToTextDrawBoxHeight(area.height))
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