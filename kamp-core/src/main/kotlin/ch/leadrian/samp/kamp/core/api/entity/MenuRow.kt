package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowListener
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class MenuRow
internal constructor(
        val menu: Menu,
        val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    private val onPlayerSelectedMenuRowListeners = LinkedHashSet<OnPlayerSelectedMenuRowListener>()

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

    fun addOnPlayerSelectedMenuRowListener(listener: OnPlayerSelectedMenuRowListener) {
        onPlayerSelectedMenuRowListeners += listener
    }

    fun removeOnPlayerSelectedMenuRowListener(listener: OnPlayerSelectedMenuRowListener) {
        onPlayerSelectedMenuRowListeners -= listener
    }

    inline fun onSelected(crossinline onSelected: MenuRow.(Player) -> Unit): OnPlayerSelectedMenuRowListener {
        val listener = object : OnPlayerSelectedMenuRowListener {

            override fun onPlayerSelectedMenuRow(player: Player, row: MenuRow) {
                onSelected.invoke(row, player)
            }
        }
        addOnPlayerSelectedMenuRowListener(listener)
        return listener
    }

    internal fun onSelected(player: Player) {
        onPlayerSelectedMenuRowListeners.forEach { it.onPlayerSelectedMenuRow(player, this) }
    }

    private fun checkColumn(column: Int) {
        if (column < 0 || column >= columnTexts.size) {
            throw IllegalArgumentException("column must be between 0 and ${columnTexts.size - 1}")
        }
    }

}