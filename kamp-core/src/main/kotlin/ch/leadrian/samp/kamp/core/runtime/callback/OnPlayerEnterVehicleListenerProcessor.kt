package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEnterVehicleListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEnterVehicleListener>(OnPlayerEnterVehicleListener::class), OnPlayerEnterVehicleListener {

    override fun onPlayerEnterVehicle(player: Player, vehicle: Vehicle, isPassenger: Boolean) {
        listeners.forEach {
            it.onPlayerEnterVehicle(player, vehicle, isPassenger)
        }
    }

}
