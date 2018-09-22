package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSirenStateChangeListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleSirenStateChangeListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleSirenStateChangeListener>(OnVehicleSirenStateChangeListener::class), OnVehicleSirenStateChangeListener {

    override fun onVehicleSirenStateChange(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, newState: ch.leadrian.samp.kamp.core.api.constants.VehicleSirenState) {
        getListeners().forEach {
            it.onVehicleSirenStateChange(player, vehicle, newState)
        }
    }

}
