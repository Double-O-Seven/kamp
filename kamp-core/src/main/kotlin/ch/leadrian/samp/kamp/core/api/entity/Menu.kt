package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import java.util.*

class Menu
internal constructor(
        val numberOfColumns: Int,
        position: Vector2D,
        columnWidth1: Float,
        columnWidth2: Float,
        val title: String,
        val locale: Locale,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) : Entity<MenuId>, AbstractDestroyable() {

    private val onExitHandlers: MutableList<Menu.(Player) -> Unit> = mutableListOf()

    override val id: MenuId
        get() = requireNotDestroyed { field }

    init {
        if (numberOfColumns != 1 && numberOfColumns != 2) throw IllegalArgumentException("Only 1 or 2 columns are supported")
        val menuId = nativeFunctionExecutor.createMenu(
                title = title,
                x = position.x,
                y = position.y,
                columns = numberOfColumns,
                col1width = columnWidth1,
                col2width = columnWidth2
        )

        if (menuId == SAMPConstants.INVALID_MENU) {
            throw CreationFailedException("Could not create menu")
        }

        id = MenuId.valueOf(menuId)
    }

    val position: Vector2D = position.toVector2D()

    private val _rows: MutableList<MenuRow> = mutableListOf()

    val rows: List<MenuRow>
        get() = _rows.toList()

    fun addItem(column: Int, text: String): MenuRow {
        checkColumn(column)
        val rowIndex = nativeFunctionExecutor.addMenuItem(menuid = id.value, column = column, menutext = text)
        val row = _rows.getOrNull(rowIndex)
                ?: MenuRow(this, rowIndex, nativeFunctionExecutor).apply { _rows.add(this) }
        row.setText(column, text)
        return row
    }

    fun addItem(column: Int, text: String, vararg args: Any): MenuRow {
        val formattedText = textFormatter.format(locale, text, *args)
        return addItem(column, formattedText)
    }

    fun addItem(column: Int, textKey: TextKey): MenuRow {
        val text = textProvider.getText(locale, textKey)
        return addItem(column, text)
    }

    fun addItem(column: Int, textKey: TextKey, vararg args: Any): MenuRow {
        val text = textProvider.getText(locale, textKey)
        return addItem(column, text, *args)
    }

    fun setColumnHeader(column: Int, text: String) {
        checkColumn(column)
        nativeFunctionExecutor.setMenuColumnHeader(
                menuid = id.value,
                column = column,
                columnheader = text
        )
    }

    fun setColumnHeader(column: Int, text: String, vararg args: Any) {
        val formattedText = textFormatter.format(locale, text, *args)
        setColumnHeader(column, formattedText)
    }

    fun setColumnHeader(column: Int, textKey: TextKey) {
        val text = textProvider.getText(locale, textKey)
        setColumnHeader(column, text)
    }

    fun setColumnHeader(column: Int, textKey: TextKey, vararg args: Any) {
        val text = textProvider.getText(locale, textKey)
        setColumnHeader(column, text, *args)
    }

    fun disable() {
        nativeFunctionExecutor.disableMenu(id.value)
    }

    fun show(forPlayer: Player) {
        nativeFunctionExecutor.showMenuForPlayer(menuid = id.value, playerid = forPlayer.id.value)
    }

    fun hide(forPlayer: Player) {
        nativeFunctionExecutor.hideMenuForPlayer(menuid = id.value, playerid = forPlayer.id.value)
    }

    fun onExit(onExit: Menu.(Player) -> Unit) {
        onExitHandlers += onExit
    }

    internal fun onExit(player: Player) {
        onExitHandlers.forEach { it.invoke(this, player) }
    }

    override fun onDestroy() {
        nativeFunctionExecutor.destroyMenu(id.value)
    }

    private fun checkColumn(column: Int) {
        if (column < 0 || column >= numberOfColumns) {
            throw IllegalArgumentException("column must be between 0 and ${numberOfColumns - 1}")
        }
    }
}