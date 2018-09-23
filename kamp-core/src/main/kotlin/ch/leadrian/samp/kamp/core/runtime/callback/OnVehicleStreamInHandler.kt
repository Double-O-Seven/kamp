package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleStreamInHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehicleStreamInListener>(OnVehicleStreamInListener::class), OnVehicleStreamInListener {

    override fun onVehicleStreamIn(vehicle: Vehicle, forPlayer: Player) {
        listeners.forEach {
            it.onVehicleStreamIn(vehicle, forPlayer)
        }
    }

}
