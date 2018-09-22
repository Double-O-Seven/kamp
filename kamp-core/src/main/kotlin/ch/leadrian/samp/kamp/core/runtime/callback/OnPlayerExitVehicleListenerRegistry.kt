package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerExitVehicleListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerExitVehicleListener>(OnPlayerExitVehicleListener::class), OnPlayerExitVehicleListener {

    override fun onPlayerExitVehicle(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle) {
        getListeners().forEach {
            it.onPlayerExitVehicle(player, vehicle)
        }
    }

}
