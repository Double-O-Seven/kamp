package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerCancelTextDrawSelectionListener {

    @IgnoredReturnValue(Result.Ignored::class)
    fun onPlayerCancelTextDrawSelection(player: Player): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object Ignored : Result(false)
    }

}
