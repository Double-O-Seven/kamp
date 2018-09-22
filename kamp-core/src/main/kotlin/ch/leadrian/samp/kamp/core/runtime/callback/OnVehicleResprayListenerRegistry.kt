package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleResprayListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleResprayListener>(OnVehicleResprayListener::class), OnVehicleResprayListener {

    override fun onVehicleRespray(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, colors: ch.leadrian.samp.kamp.core.api.data.VehicleColors) {
        getListeners().forEach {
            it.onVehicleRespray(player, vehicle, colors)
        }
    }

}
