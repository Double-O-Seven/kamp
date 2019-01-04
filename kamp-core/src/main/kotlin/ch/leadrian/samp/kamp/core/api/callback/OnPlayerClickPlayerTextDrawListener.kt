package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.annotations.InlineCallback
import ch.leadrian.samp.kamp.annotations.Receiver
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerClickPlayerTextDrawListener {

    @InlineCallback("onClick")
    @IgnoredReturnValue(Result.NotFound::class)
    fun onPlayerClickPlayerTextDraw(@Receiver textDraw: PlayerTextDraw): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object NotFound : Result(false)
    }

}
