package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerCommandTextListener {

    @IgnoredReturnValue(Result.UnknownCommand::class)
    fun onPlayerCommandText(player: Player, commandText: String): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object UnknownCommand : Result(false)
    }

}
