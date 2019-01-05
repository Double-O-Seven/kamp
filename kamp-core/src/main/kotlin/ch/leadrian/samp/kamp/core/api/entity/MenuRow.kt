package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowReceiver
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerSelectedMenuRowReceiverDelegate

class MenuRow
internal constructor(
        val menu: Menu,
        val index: Int,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val onPlayerSelectedMenuRowReceiver: OnPlayerSelectedMenuRowReceiverDelegate = OnPlayerSelectedMenuRowReceiverDelegate()
) : OnPlayerSelectedMenuRowReceiver by onPlayerSelectedMenuRowReceiver {

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

    internal fun onSelected(player: Player) {
        onPlayerSelectedMenuRowReceiver.onPlayerSelectedMenuRow(player, this)
    }

    private fun checkColumn(column: Int) {
        if (column < 0 || column >= columnTexts.size) {
            throw IllegalArgumentException("column must be between 0 and ${columnTexts.size - 1}")
        }
    }

}