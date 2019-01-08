package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels

open class HorizontalListView<T>(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory,
        adapter: ListViewAdapter<T>
) : ListView<T>(player, viewContext, adapter) {

    private val scrollBarView: HorizontalScrollBarView

    private val contentView: View

    override val itemViews: MutableList<ListItemView<T>> = mutableListOf()

    var scrollBarPosition: ScrollBarPosition = ScrollBarPosition.TOP

    var scrollBarHeight: ViewDimension = 12.pixels()
        set(value) {
            field = value
            scrollBarView.height = value
        }

    init {
        with(viewFactory) {
            scrollBarView = horizontalScrollBarView(this@HorizontalListView) {
                height = scrollBarHeight
            }
            contentView = view {
                val numberOfDisplayedItems = adapter.numberOfDisplayedItems
                val width = (100f / numberOfDisplayedItems.toFloat())
                (0 until numberOfDisplayedItems).forEach { i ->
                    view {
                        this.width = width.percent()
                        left = (width * i).percent()
                        val itemView = adapter.createView(player)
                        addChild(itemView)
                        itemViews.add(itemView)
                    }
                }
            }
        }
        updateItemViews(scrollBarView.currentPosition)
    }

    override fun onScroll(view: ScrollBarView, oldPosition: Int, newPosition: Int) {
        updateItemViews(newPosition)
        contentView.draw()
    }

    override fun onDraw() {
        super.onDraw()
        if (isInvalidated) {
            when (scrollBarPosition) {
                ScrollBarPosition.TOP -> {
                    scrollBarView.top = 0.pixels()
                    scrollBarView.bottom = null
                    contentView topToBottomOf scrollBarView
                    contentView.bottom = 0.pixels()
                }
                ScrollBarPosition.BOTTOM -> {
                    scrollBarView.top = null
                    scrollBarView.bottom = 0.pixels()
                    contentView.top = 0.pixels()
                    contentView bottomToTopOf scrollBarView
                }
            }
        }
    }

    enum class ScrollBarPosition {
        TOP,
        BOTTOM
    }

}