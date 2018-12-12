package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View

abstract class ListView<T>(
        player: Player,
        viewContext: ViewContext,
        private val adapter: ListViewAdapter<T>
) : View(player, viewContext), ScrollBarAdapter {

    final override val numberOfTicks: Int
        get() = adapter.numberOfItems

    final override val windowSize: Int = adapter.numberOfDisplayedItems

    protected abstract val itemViews: List<ListItemView<T>>

    protected fun updateItemViews(scrollBarPosition: Int) {
        itemViews.forEachIndexed { i, itemView ->
            val position = scrollBarPosition + i
            if (position >= adapter.numberOfItems) {
                itemView.hide()
            } else {
                itemView.apply {
                    setItem(position, adapter.getItem(position))
                    show(draw = false)
                }
            }
        }
    }

}