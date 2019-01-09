package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnDialogResponseListener {

    @IgnoredReturnValue(Result.Ignored::class)
    fun onDialogResponse(player: Player, dialogId: DialogId, response: DialogResponse, listItem: Int, inputText: String): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object Ignored : Result(false)

    }
}