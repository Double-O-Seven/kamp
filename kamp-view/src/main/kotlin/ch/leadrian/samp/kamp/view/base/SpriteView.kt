package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.layout.screenHeightToTextDrawBoxHeight
import ch.leadrian.samp.kamp.view.layout.screenYCoordinateToTextDrawBoxY

open class SpriteView(
        player: Player,
        viewContext: ViewContext,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private var spriteNameSupplier: () -> String = { "LD_SPAC:white" }

    var spriteName: String
        get() = spriteNameSupplier()
        set(value) {
            spriteNameSupplier = { value }
        }

    fun spriteName(spriteNameSupplier: () -> String) {
        this.spriteNameSupplier = spriteNameSupplier
    }

    private var colorSupplier: () -> Color = { Colors.WHITE }

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
                spriteName,
                vector2DOf(x = area.minX, y = screenYCoordinateToTextDrawBoxY(area.minY))
        ).also {
            it.alignment = TextDrawAlignment.CENTERED
            it.textSize = vector2DOf(x = area.width, y = screenHeightToTextDrawBoxHeight(area.height) / 0.135f)
            it.font = TextDrawFont.SPRITE_DRAW
            it.backgroundColor = Colors.TRANSPARENT
            it.color = color
            it.onClick { click() }
            it.show()
        }
    }

    private fun updateTextDraw() {
        textDraw?.also {
            var show = false

            if (it.text != spriteName) {
                it.text = spriteName
                show = false
            }

            if (it.color != color) {
                it.color = color
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