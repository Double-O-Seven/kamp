package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class MenuRow
internal constructor(
        val menu: Menu,
        val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    private val onSelectedHandlers: MutableList<MenuRow.(Player) -> Unit> = mutableListOf()

    private val columnTexts: Array<String?> = arrayOfNulls(menu.numberOfColumns)

    fun disable() {
        nativeFunctionExecutor.disableMenuRow(menuid = menu.id.value, row = index)
    }

    fun getText(column: Int): String? {
        checkColumn(column)
        return columnTexts[column]
    }

    internal fun setText(column: Int, text: String) {
        checkColumn(column)
        columnTexts[column] = text
    }

    fun onSelected(onSelected: MenuRow.(Player) -> Unit) {
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