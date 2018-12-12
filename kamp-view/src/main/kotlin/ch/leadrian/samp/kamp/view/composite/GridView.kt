package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent

open class GridView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory,
        adapter: GridViewAdapter
) : View(player, viewContext) {

    init {
        val numberOfRows = adapter.numberOfRows
        val numberOfColumns = adapter.numberOfColumns
        val cellHeight = 100f / numberOfRows
        val cellWidth = 100f / numberOfColumns
        with(viewFactory) {
            (0 until numberOfRows).forEach { row ->
                (0 until numberOfColumns).forEach { column ->
                    this@GridView.view {
                        top = (row * cellHeight).percent()
                        left = (column * cellWidth).percent()
                        height = cellHeight.percent()
                        width = cellWidth.percent()
                        addChild(adapter.createView(player = player, row = row, column = column))
                    }
                }
            }
        }
    }

}