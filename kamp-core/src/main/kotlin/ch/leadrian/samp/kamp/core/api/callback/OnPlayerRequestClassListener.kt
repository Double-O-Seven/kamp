package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerRequestClassListener {

    @IgnoredReturnValue(Result.Allow::class)
    fun onPlayerRequestClass(player: Player, playerClass: PlayerClass): Result

    sealed class Result(val value: Boolean) {

        object Allow : Result(true)

        object PreventSpawn : Result(false)
    }

}
