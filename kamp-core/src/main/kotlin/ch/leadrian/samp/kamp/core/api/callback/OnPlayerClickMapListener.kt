package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnPlayerClickMapListener {

    @IgnoredReturnValue(Result.Continue::class)
    fun onPlayerClickMap(player: Player, coordinates: Vector3D): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object Continue : Result(false)
    }

}
