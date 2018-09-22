package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener
import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener.Result
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnUnoccupiedVehicleUpdateListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnUnoccupiedVehicleUpdateListener>(OnUnoccupiedVehicleUpdateListener::class), OnUnoccupiedVehicleUpdateListener {

    override fun onUnoccupiedVehicleUpdate(vehicle: Vehicle, player: Player, passengerSeat: Int?, coordinates: Vector3D, velocity: Vector3D): Result {
        return listeners.map {
            it.onUnoccupiedVehicleUpdate(vehicle, player, passengerSeat, coordinates, velocity)
        }.firstOrNull { it == Result.Desync } ?: Result.Sync
    }

}
