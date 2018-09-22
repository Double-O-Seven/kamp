package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleStreamOutListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnVehicleStreamOutListener>(OnVehicleStreamOutListener::class), OnVehicleStreamOutListener {

    override fun onVehicleStreamOut(vehicle: Vehicle, forPlayer: Player) {
        listeners.forEach {
            it.onVehicleStreamOut(vehicle, forPlayer)
        }
    }

}
