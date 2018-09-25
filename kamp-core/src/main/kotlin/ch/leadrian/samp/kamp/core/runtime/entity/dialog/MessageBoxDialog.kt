package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class MessageBoxDialog(
        id: DialogId,
        private val captionTextSupplier: DialogTextSupplier,
        private val leftButtonTextSupplier: DialogTextSupplier,
        private val rightButtonTextSupplier: DialogTextSupplier,
        private val messageTextSupplier: DialogTextSupplier,
        private val onClickLeftButton: Dialog.(Player) -> Unit,
        private val onClickRightButton: Dialog.(Player) -> Unit,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : AbstractDialog(id) {

    override fun show(forPlayer: Player) {
        nativeFunctionExecutor.showPlayerDialog(
                dialogid = id.value,
                playerid = forPlayer.id.value,
                style = DialogStyle.MSGBOX.value,
                button1 = leftButtonTextSupplier.getText(forPlayer),
                button2 = rightButtonTextSupplier.getText(forPlayer),
                caption = captionTextSupplier.getText(forPlayer),
                info = messageTextSupplier.getText(forPlayer)
        )
    }

    override fun onResponse(player: Player, response: DialogResponse, listItem: Int, inputText: String) {
        when (response) {
            DialogResponse.LEFT_BUTTON -> onClickLeftButton(player)
            DialogResponse.RIGHT_BUTTON -> onClickRightButton(player)
        }
    }
}