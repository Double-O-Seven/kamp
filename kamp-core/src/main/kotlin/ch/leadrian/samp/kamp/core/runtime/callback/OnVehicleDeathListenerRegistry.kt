package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleDeathListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleDeathListener>(OnVehicleDeathListener::class), OnVehicleDeathListener {

    override fun onVehicleDeath(vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, killer: ch.leadrian.samp.kamp.core.api.entity.Player) {
        getListeners().forEach {
            it.onVehicleDeath(vehicle, killer)
        }
    }

}
