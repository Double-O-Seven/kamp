package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerClickPlayerListener {

    @IgnoredReturnValue(Result.Continue::class)
    fun onPlayerClickPlayer(
            player: Player,
            clickedPlayer: Player,
            source: ClickPlayerSource
    ): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object Continue : Result(false)
    }

}
