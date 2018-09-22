package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleSpawnListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnVehicleSpawnListener>(OnVehicleSpawnListener::class), OnVehicleSpawnListener {

    override fun onVehicleSpawn(vehicle: Vehicle) {
        listeners.forEach {
            it.onVehicleSpawn(vehicle)
        }
    }

}
