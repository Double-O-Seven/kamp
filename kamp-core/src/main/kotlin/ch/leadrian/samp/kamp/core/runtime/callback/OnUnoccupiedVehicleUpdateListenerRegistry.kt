package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnUnoccupiedVehicleUpdateListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnUnoccupiedVehicleUpdateListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnUnoccupiedVehicleUpdateListener>(OnUnoccupiedVehicleUpdateListener::class), OnUnoccupiedVehicleUpdateListener {

    override fun onUnoccupiedVehicleUpdate(vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, player: ch.leadrian.samp.kamp.core.api.entity.Player, passengerSeat: kotlin.Int?, coordinates: ch.leadrian.samp.kamp.core.api.data.Vector3D, velocity: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onUnoccupiedVehicleUpdate(vehicle, player, passengerSeat, coordinates, velocity)
        }
    }

}
