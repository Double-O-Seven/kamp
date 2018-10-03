package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDamageStatusUpdateListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleDamageStatusUpdateHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehicleDamageStatusUpdateListener>(OnVehicleDamageStatusUpdateListener::class), OnVehicleDamageStatusUpdateListener {

    override fun onVehicleDamageStatusUpdate(vehicle: Vehicle, player: Player) {
        return listeners.forEach {
            it.onVehicleDamageStatusUpdate(vehicle, player)
        }
    }

}
