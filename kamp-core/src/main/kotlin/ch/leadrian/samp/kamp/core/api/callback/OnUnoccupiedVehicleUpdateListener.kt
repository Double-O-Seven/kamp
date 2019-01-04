package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.annotations.IgnoredReturnValue
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnUnoccupiedVehicleUpdateListener {

    @IgnoredReturnValue(Result.Sync::class)
    fun onUnoccupiedVehicleUpdate(
            vehicle: Vehicle,
            player: Player,
            passengerSeat: Int?,
            coordinates: Vector3D,
            velocity: Vector3D
    ): Result

    sealed class Result(val value: Boolean) {

        object Sync : Result(true)

        object Desync : Result(false)
    }

}
