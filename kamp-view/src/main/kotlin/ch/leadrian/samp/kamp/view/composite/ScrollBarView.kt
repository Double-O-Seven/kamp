package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View

abstract class ScrollBarView(
        player: Player,
        viewContext: ViewContext
) : View(player, viewContext) {

    lateinit var scrollBarAdapter: ScrollBarAdapter

    var currentPosition: Int = 0
        private set

    abstract var color: Color

    abstract var backgroundColor: Color

    fun scroll(ticks: Int) {
        val oldPosition = currentPosition
        currentPosition += ticks
        adjustCurrentPosition()
        onScroll()
        scrollBarAdapter.onScroll(view = this, oldPosition = oldPosition, newPosition = currentPosition)
    }

    override fun onDraw() {
        adjustCurrentPosition()
    }

    private fun adjustCurrentPosition() {
        currentPosition = currentPosition.coerceIn(0, Math.max(0, scrollBarAdapter.numberOfTicks - scrollBarAdapter.windowSize))
    }

    protected abstract fun onScroll()

}