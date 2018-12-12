package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext

open class SpriteView(
        player: Player,
        viewContext: ViewContext,
        private val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private var spriteNameSupplier: ValueSupplier<String> = ValueSupplier("LD_SPAC:white")

    var spriteName: String by spriteNameSupplier

    fun spriteName(spriteNameSupplier: () -> String) {
        this.spriteNameSupplier.value(spriteNameSupplier)
    }

    private var colorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.WHITE)

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
                spriteName,
                vector2DOf(
                        x = area.minX,
                        y = screenMinYToTextDrawMinY(area.minY, offset = 0f)
                )
        ).also {
            it.textSize = vector2DOf(
                    x = area.width,
                    y = screenHeightToTextDrawHeight(area.height, offset = 0f)
            )
            it.isSelectable = isEnabled
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

            if (it.isSelectable != isEnabled) {
                it.isSelectable = isEnabled
                show = true
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