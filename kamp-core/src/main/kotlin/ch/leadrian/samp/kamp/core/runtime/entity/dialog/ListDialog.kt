package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class ListDialog<V>(
        id: DialogId,
        private val captionTextSupplier: DialogTextSupplier,
        private val leftButtonTextSupplier: DialogTextSupplier,
        private val rightButtonTextSupplier: DialogTextSupplier,
        private val onSelectItem: (Dialog.(Player, ListDialogItem<V>, String) -> Unit)?,
        private val onCancel: Dialog.(Player) -> Unit,
        private val items: List<ListDialogItem<V>>,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : AbstractDialog(id) {

    private companion object {

        val log = loggerFor<ListDialog<*>>()

    }

    override fun show(forPlayer: Player) {
        val itemsString = items.joinToString("\n") { it.getContent(forPlayer) }
        log.debug("Dialog {}: Showing for player {}: {}", id.value, forPlayer.id.value, itemsString)
        nativeFunctionExecutor.showPlayerDialog(
                dialogid = id.value,
                playerid = forPlayer.id.value,
                caption = captionTextSupplier.getText(forPlayer),
                button1 = leftButtonTextSupplier.getText(forPlayer),
                button2 = rightButtonTextSupplier.getText(forPlayer),
                style = DialogStyle.LIST.value,
                info = itemsString
        )
    }

    override fun onResponse(player: Player, response: DialogResponse, listItem: Int, inputText: String) {
        when (response) {
            DialogResponse.LEFT_BUTTON -> {
                val item = items.getOrNull(listItem)
                if (item != null) {
                    item.onSelect(player, inputText)
                    onSelectItem?.invoke(this, player, item, inputText)
                } else {
                    log.warn(
                            "Dialog {}: Invalid dialog item selected by player {}: {}, {} items available",
                            id.value,
                            player.name,
                            listItem,
                            items.size
                    )
                    onCancel(this, player)
                }
            }
            DialogResponse.RIGHT_BUTTON -> onCancel(this, player)
        }
    }
}