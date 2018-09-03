package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.constants.MenuColumn
import ch.leadrian.samp.kamp.api.entity.id.MenuId
import ch.leadrian.samp.kamp.api.text.TextKey

interface Menu : Destroyable, Entity<MenuId> {

    override val id: MenuId

    val rows: List<MenuRow>

    fun addItem(column: MenuColumn, text: String): MenuRow

    fun addItem(column: MenuColumn, text: String, vararg args: Any): MenuRow

    fun addItem(column: MenuColumn, textKey: TextKey): MenuRow

    fun addItem(column: MenuColumn, textKey: TextKey, vararg args: Any): MenuRow

    fun setColumnHeader(column: MenuColumn, text: String)

    fun setColumnHeader(column: MenuColumn, text: String, vararg args: Any)

    fun setColumnHeader(column: MenuColumn, textKey: TextKey)

    fun setColumnHeader(column: MenuColumn, textKey: TextKey, vararg args: Any)

    fun disable()

    fun show(forPlayer: Player)

    fun hide(forPlayer: Player)

    fun onSelectedMenuRow(onSelectedMenuRow: Menu.(Player, MenuRow) -> Unit)

    fun onExit(onExit: Menu.(Player) -> Unit)

}