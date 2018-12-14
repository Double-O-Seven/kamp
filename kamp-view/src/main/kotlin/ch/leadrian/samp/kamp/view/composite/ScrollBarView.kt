package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.style.ScrollBarStyle
import ch.leadrian.samp.kamp.view.style.Style

abstract class ScrollBarView(
        player: Player,
        viewContext: ViewContext,
        private val adapter: ScrollBarAdapter
) : View(player, viewContext) {

    var currentPosition: Int = 0
        private set

    abstract var color: Color

    abstract fun color(colorSupplier: () -> Color)

    abstract var backgroundColor: Color

    abstract fun backgroundColor(backgroundColorSupplier: () -> Color)

    abstract var buttonColor: Color

    abstract fun buttonColor(buttonColorSupplier: () -> Color)

    fun scroll(ticks: Int) {
        val oldPosition = currentPosition
        currentPosition += ticks
        adjustCurrentPosition()
        onScroll()
        adapter.onScroll(view = this, oldPosition = oldPosition, newPosition = currentPosition)
    }

    protected open fun onScroll() {
        invalidate()
        draw()
    }

    override fun onDraw() {
        adjustCurrentPosition()
    }

    private fun adjustCurrentPosition() {
        currentPosition = currentPosition.coerceIn(0, Math.max(0, adapter.numberOfTicks - adapter.windowSize))
    }

    override fun applyStyle(style: Style): Boolean {
        super.applyStyle(style)
        if (style is ScrollBarStyle) {
            color = style.scrollBarColor
            backgroundColor = style.scrollBarBackgroundColor
            buttonColor = style.scrollBarButtonColor
        }
        return true
    }

}