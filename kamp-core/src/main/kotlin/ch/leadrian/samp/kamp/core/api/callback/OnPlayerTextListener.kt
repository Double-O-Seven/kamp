package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerTextListener {

    @IgnoredReturnValue(Result.Allowed::class)
    fun onPlayerText(player: Player, text: String): Result

    sealed class Result(val value: Boolean) {

        object Allowed : Result(true)

        object Blocked : Result(false)
    }

}
