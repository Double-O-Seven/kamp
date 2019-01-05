package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerClickTextDrawListener {

    @InlineCallback("onClick")
    @IgnoredReturnValue(Result.NotFound::class)
    fun onPlayerClickTextDraw(player: Player, @Receiver textDraw: TextDraw): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object NotFound : Result(false)
    }

}
