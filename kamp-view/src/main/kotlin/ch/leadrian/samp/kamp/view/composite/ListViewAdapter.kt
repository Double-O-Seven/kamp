package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player

interface ListViewAdapter<T> {

    val numberOfItems: Int

    val numberOfDisplayedItems: Int

    fun getItem(position: Int): T

    fun createView(player: Player): ListItemView<T>

}