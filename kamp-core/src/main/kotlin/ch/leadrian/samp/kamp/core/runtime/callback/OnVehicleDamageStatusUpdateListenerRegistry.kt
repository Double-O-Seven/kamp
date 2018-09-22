package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleDamageStatusUpdateListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleDamageStatusUpdateListener>(OnVehicleDamageStatusUpdateListener::class), OnVehicleDamageStatusUpdateListener {

    override fun onVehicleDamageStatusUpdate(vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, player: ch.leadrian.samp.kamp.core.api.entity.Player): ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener.Result {
        getListeners().forEach {
            it.onVehicleDamageStatusUpdate(vehicle, player)
        }
    }

}
