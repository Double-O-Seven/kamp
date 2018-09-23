package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSirenStateChangeListener
import ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleSirenStateChangeHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehicleSirenStateChangeListener>(OnVehicleSirenStateChangeListener::class), OnVehicleSirenStateChangeListener {

    override fun onVehicleSirenStateChange(player: Player, vehicle: Vehicle, newState: VehicleSirenState) {
        listeners.forEach {
            it.onVehicleSirenStateChange(player, vehicle, newState)
        }
    }

}
