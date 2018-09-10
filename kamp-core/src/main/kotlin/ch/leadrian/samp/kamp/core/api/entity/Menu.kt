package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import java.util.*

interface Menu : Destroyable, Entity<MenuId> {

    override val id: MenuId

    val locale: Locale

    val title: String

    val numberOfColumns: Int

    val position: Vector2D

    val rows: List<MenuRow>

    fun addItem(column: Int, text: String): MenuRow

    fun addItem(column: Int, text: String, vararg args: Any): MenuRow

    fun addItem(column: Int, textKey: TextKey): MenuRow

    fun addItem(column: Int, textKey: TextKey, vararg args: Any): MenuRow

    fun setColumnHeader(column: Int, text: String)

    fun setColumnHeader(column: Int, text: String, vararg args: Any)

    fun setColumnHeader(column: Int, textKey: TextKey)

    fun setColumnHeader(column: Int, textKey: TextKey, vararg args: Any)

    fun disable()

    fun show(forPlayer: Player)

    fun hide(forPlayer: Player)

    fun onSelectedMenuRow(onSelectedMenuRow: Menu.(Player, MenuRow) -> Unit)

    fun onExit(onExit: Menu.(Player) -> Unit)

}