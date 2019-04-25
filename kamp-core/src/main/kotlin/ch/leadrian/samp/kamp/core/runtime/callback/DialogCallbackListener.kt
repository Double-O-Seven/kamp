package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DialogCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager,
        private val dialogRegistry: DialogRegistry
) : OnDialogResponseListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onDialogResponse(
            player: Player,
            dialogId: DialogId,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): OnDialogResponseListener.Result {
        val dialog = dialogRegistry[dialogId]
        player.resetCurrentDialog()
        if (dialog != null) {
            return propagateDialogResponse(dialog, player, response, listItem, inputText)
        }
        return OnDialogResponseListener.Result.Ignored
    }

    private fun propagateDialogResponse(
            dialog: AbstractDialog,
            player: Player,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): OnDialogResponseListener.Result {
        val result = dialog.onResponse(player, response, listItem, inputText)
        if (response.isCancelButton && result == OnDialogResponseListener.Result.Ignored && dialog === player.dialogNavigation.top) {
            player.dialogNavigation.pop()
            return OnDialogResponseListener.Result.Processed
        }
        return result
    }

}