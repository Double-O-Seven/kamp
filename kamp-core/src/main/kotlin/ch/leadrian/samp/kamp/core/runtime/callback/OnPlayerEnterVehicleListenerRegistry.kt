package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerEnterVehicleListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerEnterVehicleListener>(OnPlayerEnterVehicleListener::class), OnPlayerEnterVehicleListener {

    override fun onPlayerEnterVehicle(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, isPassenger: kotlin.Boolean) {
        getListeners().forEach {
            it.onPlayerEnterVehicle(player, vehicle, isPassenger)
        }
    }

}
