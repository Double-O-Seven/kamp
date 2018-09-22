package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleStreamInListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleStreamInListener>(OnVehicleStreamInListener::class), OnVehicleStreamInListener {

    override fun onVehicleStreamIn(vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, forPlayer: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onVehicleStreamIn(vehicle, forPlayer)
        }
    }

}
