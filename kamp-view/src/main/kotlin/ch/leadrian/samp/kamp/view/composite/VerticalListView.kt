package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels

open class VerticalListView<T>(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory,
        adapter: ListViewAdapter<T>
) : ListView<T>(player, viewContext, adapter) {

    private lateinit var scrollBarView: VerticalScrollBarView

    private lateinit var contentView: View

    override val itemViews: MutableList<ListItemView<T>> = mutableListOf()

    var scrollBarPosition: ScrollBarPosition = ScrollBarPosition.LEFT

    var scrollBarWidth: ViewDimension = 12.pixels()
        set(value) {
            field = value
            scrollBarView.width = value
        }

    init {
        with(viewFactory) {
            scrollBarView = verticalScrollBarView(this@VerticalListView) {
                width = scrollBarWidth
            }
            contentView = view {
                val numberOfDisplayedItems = adapter.numberOfDisplayedItems
                val height = (100f / numberOfDisplayedItems.toFloat())
                (0 until numberOfDisplayedItems).forEach { i ->
                    view {
                        this.height = height.percent()
                        top = (height * i).percent()
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
                ScrollBarPosition.LEFT -> {
                    scrollBarView.left = 0.pixels()
                    scrollBarView.right = null
                    contentView leftToRightOf scrollBarView
                    contentView.right = 0.pixels()
                }
                ScrollBarPosition.RIGHT -> {
                    scrollBarView.left = null
                    scrollBarView.right = 0.pixels()
                    contentView.left = 0.pixels()
                    contentView rightToLeftOf scrollBarView
                }
            }
        }
    }

    enum class ScrollBarPosition {
        LEFT,
        RIGHT
    }

}