package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleSpawnListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleSpawnListener>(OnVehicleSpawnListener::class), OnVehicleSpawnListener {

    override fun onVehicleSpawn(vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle): kotlin.Boolean {
        getListeners().forEach {
            it.onVehicleSpawn(vehicle)
        }
    }

}
