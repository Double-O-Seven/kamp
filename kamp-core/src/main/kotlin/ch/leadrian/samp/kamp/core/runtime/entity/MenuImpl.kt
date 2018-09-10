package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import java.util.*

internal class MenuImpl(
        override val numberOfColumns: Int,
        position: Vector2D,
        columnWidth1: Float,
        columnWidth2: Float,
        override val title: String,
        override val locale: Locale,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) : Menu {

    private val onDestroyHandlers: MutableList<MenuImpl.() -> Unit> = mutableListOf()

    private val onSelectedMenuRowHandlers: MutableList<Menu.(Player, MenuRow) -> Unit> = mutableListOf()

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

    override val position: Vector2D = position.toVector2D()

    override val rows: MutableList<MenuRowImpl> = mutableListOf()

    override fun addItem(column: Int, text: String): MenuRowImpl {
        checkColumn(column)
        val rowIndex = nativeFunctionExecutor.addMenuItem(menuid = id.value, column = column, menutext = text)
        val row = rows.getOrNull(rowIndex)
                ?: MenuRowImpl(this, rowIndex, nativeFunctionExecutor).apply { rows.add(this) }
        row.setText(column, text)
        return row
    }

    override fun addItem(column: Int, text: String, vararg args: Any): MenuRowImpl {
        val formattedText = textFormatter.format(locale, text, *args)
        return addItem(column, formattedText)
    }

    override fun addItem(column: Int, textKey: TextKey): MenuRowImpl {
        val text = textProvider.getText(locale, textKey)
        return addItem(column, text)
    }

    override fun addItem(column: Int, textKey: TextKey, vararg args: Any): MenuRowImpl {
        val text = textProvider.getText(locale, textKey)
        return addItem(column, text, *args)
    }

    override fun setColumnHeader(column: Int, text: String) {
        checkColumn(column)
        nativeFunctionExecutor.setMenuColumnHeader(
                menuid = id.value,
                column = column,
                columnheader = text
        )
    }

    override fun setColumnHeader(column: Int, text: String, vararg args: Any) {
        val formattedText = textFormatter.format(locale, text, *args)
        setColumnHeader(column, formattedText)
    }

    override fun setColumnHeader(column: Int, textKey: TextKey) {
        val text = textProvider.getText(locale, textKey)
        setColumnHeader(column, text)
    }

    override fun setColumnHeader(column: Int, textKey: TextKey, vararg args: Any) {
        val text = textProvider.getText(locale, textKey)
        setColumnHeader(column, text, *args)
    }

    override fun disable() {
        nativeFunctionExecutor.disableMenu(id.value)
    }

    override fun show(forPlayer: Player) {
        nativeFunctionExecutor.showMenuForPlayer(menuid = id.value, playerid = forPlayer.id.value)
    }

    override fun hide(forPlayer: Player) {
        nativeFunctionExecutor.hideMenuForPlayer(menuid = id.value, playerid = forPlayer.id.value)
    }

    override fun onSelectedMenuRow(onSelectedMenuRow: Menu.(Player, MenuRow) -> Unit) {
        onSelectedMenuRowHandlers += onSelectedMenuRow
    }

    internal fun onSelectedMenuRow(player: Player, rowIndex: Int) {
        val menuRow = rows[rowIndex]
        menuRow.onSelected(player)
        onSelectedMenuRowHandlers.forEach { it.invoke(this, player, menuRow) }
    }

    override fun onExit(onExit: Menu.(Player) -> Unit) {
        onExitHandlers += onExit
    }

    internal fun onExit(player: Player) {
        onExitHandlers.forEach { it.invoke(this, player) }
    }

    internal fun onDestroy(onDestroy: MenuImpl.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.destroyMenu(id.value)
        isDestroyed = true
    }

    private fun checkColumn(column: Int) {
        if (column < 0 || column >= numberOfColumns) {
            throw IllegalArgumentException("column must be between 0 and ${numberOfColumns - 1}")
        }
    }
}