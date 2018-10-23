package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDestructionListener
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleDestructionHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehicleDestructionListener>(OnVehicleDestructionListener::class), OnVehicleDestructionListener {

    override fun onVehicleDestruction(vehicle: Vehicle) {
        listeners.forEach {
            it.onVehicleDestruction(vehicle)
        }
    }

}
