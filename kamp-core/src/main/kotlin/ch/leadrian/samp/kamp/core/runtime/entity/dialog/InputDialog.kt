package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidator
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class InputDialog(
        id: DialogId,
        private val captionTextSupplier: DialogTextSupplier,
        private val leftButtonTextSupplier: DialogTextSupplier,
        private val rightButtonTextSupplier: DialogTextSupplier,
        private val messageTextSupplier: DialogTextSupplier,
        private val isPasswordInput: Dialog.(Player) -> Boolean,
        private val validators: List<DialogInputValidator>,
        private val onSubmit: Dialog.(Player, String) -> Unit,
        private val onInvalidInput: Dialog.(Player, Any) -> Unit,
        private val onCancel: Dialog.(Player) -> Unit,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : AbstractDialog(id) {

    override fun show(forPlayer: Player) {
        val style = when {
            isPasswordInput(this, forPlayer) -> DialogStyle.PASSWORD.value
            else -> DialogStyle.INPUT.value
        }
        nativeFunctionExecutor.showPlayerDialog(
                dialogid = id.value,
                playerid = forPlayer.id.value,
                style = style,
                button1 = leftButtonTextSupplier.getText(forPlayer),
                button2 = rightButtonTextSupplier.getText(forPlayer),
                caption = captionTextSupplier.getText(forPlayer),
                info = messageTextSupplier.getText(forPlayer)
        )
    }

    override fun onResponse(player: Player, response: DialogResponse, listItem: Int, inputText: String) {
        when (response) {
            DialogResponse.LEFT_BUTTON -> {
                val validationError = validators.asSequence().mapNotNull {
                    it.validate(player, inputText)
                }.firstOrNull()
                when {
                    validationError != null -> onInvalidInput(player, validationError)
                    else -> onSubmit(player, inputText)
                }
            }
            DialogResponse.RIGHT_BUTTON -> onCancel(player)
        }
    }
}