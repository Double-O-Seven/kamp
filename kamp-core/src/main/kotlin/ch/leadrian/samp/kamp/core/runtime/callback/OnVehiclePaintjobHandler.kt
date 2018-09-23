package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehiclePaintjobListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehiclePaintjobHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehiclePaintjobListener>(OnVehiclePaintjobListener::class), OnVehiclePaintjobListener {

    override fun onVehiclePaintjob(player: Player, vehicle: Vehicle, paintjobId: Int) {
        listeners.forEach {
            it.onVehiclePaintjob(player, vehicle, paintjobId)
        }
    }

}
