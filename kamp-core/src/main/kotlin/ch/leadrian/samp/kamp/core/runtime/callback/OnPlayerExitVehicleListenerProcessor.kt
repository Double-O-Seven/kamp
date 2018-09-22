package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerExitVehicleListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerExitVehicleListener>(OnPlayerExitVehicleListener::class), OnPlayerExitVehicleListener {

    override fun onPlayerExitVehicle(player: Player, vehicle: Vehicle) {
        listeners.forEach {
            it.onPlayerExitVehicle(player, vehicle)
        }
    }

}
