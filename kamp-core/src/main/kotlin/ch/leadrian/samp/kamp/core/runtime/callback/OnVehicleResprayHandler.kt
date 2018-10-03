package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleResprayHandler
@Inject
constructor() : CallbackListenerRegistry<OnVehicleResprayListener>(OnVehicleResprayListener::class), OnVehicleResprayListener {

    override fun onVehicleRespray(player: Player, vehicle: Vehicle, colors: VehicleColors): OnVehicleResprayListener.Result {
        return listeners.map {
            it.onVehicleRespray(player, vehicle, colors)
        }.firstOrNull { it == OnVehicleResprayListener.Result.Desync } ?: OnVehicleResprayListener.Result.Sync
    }

}
