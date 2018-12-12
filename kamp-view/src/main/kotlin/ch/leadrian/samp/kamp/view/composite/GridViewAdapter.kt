package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.base.View

interface GridViewAdapter {

    val numberOfRows: Int

    val numberOfColumns: Int

    fun createView(player: Player, row: Int, column: Int): View

}