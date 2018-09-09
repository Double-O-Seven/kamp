package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.entity.Menu
import ch.leadrian.samp.kamp.api.entity.MenuRow
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor

internal class MenuRowImpl(
        override val menu: Menu,
        override val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : MenuRow {

    private val onSelectedHandlers: MutableList<MenuRow.(Player) -> Unit> = mutableListOf()

    private val columnTexts: Array<String?> = arrayOfNulls(menu.numberOfColumns)

    override fun disable() {
        nativeFunctionExecutor.disableMenuRow(menuid = menu.id.value, row = index)
    }

    override fun getText(column: Int): String? {
        checkColumn(column)
        return columnTexts[column]
    }

    internal fun setText(column: Int, text: String) {
        checkColumn(column)
        columnTexts[column] = text
    }

    override fun onSelected(onSelected: MenuRow.(Player) -> Unit) {
        onSelectedHandlers += onSelected
    }

    internal fun onSelected(player: Player) {
        onSelectedHandlers.forEach { it.invoke(this, player) }
    }

    private fun checkColumn(column: Int) {
        if (column < 0 || column >= columnTexts.size) {
            throw IllegalArgumentException("column must be between 0 and ${columnTexts.size - 1}")
        }
    }

}