package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService

open class BackgroundView(
        player: Player,
        viewContext: ViewContext,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    var color: Color = colorOf(0x00000080)

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
                vector2DOf(x = area.minX + TEXT_DRAW_OFFSET_LEFT_SIDE, y = area.minY)
        ).apply {
            textSize = vector2DOf(x = area.minX + area.width, y = area.height)
            letterSize = vector2DOf(x = 0f, y = pixelsToLetterSize(area.height))
            useBox = true
            boxColor = this@BackgroundView.color
            isSelectable = this@BackgroundView.isEnabled
            onClick { click() }
            show()
        }
    }

    private fun updateTextDraw() {
        textDraw?.apply {
            var show = false

            if (boxColor != this@BackgroundView.color) {
                boxColor = this@BackgroundView.color
                show = true
            }

            if (isSelectable != this@BackgroundView.isEnabled) {
                isSelectable = this@BackgroundView.isEnabled
                show = true
            }

            if (show) {
                show()
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