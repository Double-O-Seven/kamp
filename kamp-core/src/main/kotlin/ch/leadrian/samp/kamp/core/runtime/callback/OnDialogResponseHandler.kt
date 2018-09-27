package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnDialogResponseHandler
@Inject
constructor() : CallbackListenerRegistry<OnDialogResponseListener>(OnDialogResponseListener::class), OnDialogResponseListener {

    override fun onDialogResponse(
            player: Player,
            dialogId: DialogId,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): OnDialogResponseListener.Result {
        return listeners.map {
            it.onDialogResponse(player, dialogId, response, listItem, inputText)
        }.firstOrNull { it == OnDialogResponseListener.Result.Processed } ?: OnDialogResponseListener.Result.Ignored
    }

}
