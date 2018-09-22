package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleStreamOutListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleStreamOutListener>(OnVehicleStreamOutListener::class), OnVehicleStreamOutListener {

    override fun onVehicleStreamOut(vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, forPlayer: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onVehicleStreamOut(vehicle, forPlayer)
        }
    }

}
