package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.MenuColumn

interface MenuRow {

    val menu: Menu

    val index: Int

    fun disable()

    fun getText(column: MenuColumn)

    fun onSelected(onSelected: MenuRow.(Player) -> Unit)

}