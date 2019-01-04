package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnRconCommandListener {

    @IgnoredReturnValue(Result.UnknownCommand::class)
    fun onRconCommand(command: String): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object UnknownCommand : Result(false)
    }

}
