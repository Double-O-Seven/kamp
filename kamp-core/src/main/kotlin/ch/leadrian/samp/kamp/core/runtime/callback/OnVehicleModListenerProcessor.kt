package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener.Result
import ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleModListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnVehicleModListener>(OnVehicleModListener::class), OnVehicleModListener {

    override fun onVehicleMod(player: Player, vehicle: Vehicle, componentModel: VehicleComponentModel): Result {
        return listeners.map {
            it.onVehicleMod(player, vehicle, componentModel)
        }.firstOrNull { it == Result.Desync } ?: Result.Sync
    }

}
