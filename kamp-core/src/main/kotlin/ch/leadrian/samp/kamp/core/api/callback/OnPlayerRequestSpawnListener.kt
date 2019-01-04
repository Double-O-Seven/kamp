package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerRequestSpawnListener {

    @IgnoredReturnValue(Result.Granted::class)
    fun onPlayerRequestSpawn(player: Player): Result

    sealed class Result(val value: Boolean) {

        object Granted : Result(true)

        object Denied : Result(false)
    }

}
