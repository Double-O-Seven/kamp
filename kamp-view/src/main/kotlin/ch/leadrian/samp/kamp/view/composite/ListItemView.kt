package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.View

abstract class ListItemView<T>(
        player: Player,
        viewContext: ViewContext
) : View(player, viewContext) {

    abstract fun setItem(position: Int, item: T)

}