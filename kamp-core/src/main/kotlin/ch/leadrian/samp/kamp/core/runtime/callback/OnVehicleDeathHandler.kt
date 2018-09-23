package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleDeathHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehicleDeathListener>(OnVehicleDeathListener::class), OnVehicleDeathListener {

    override fun onVehicleDeath(vehicle: Vehicle, killer: Player) {
        listeners.forEach {
            it.onVehicleDeath(vehicle, killer)
        }
    }

}
