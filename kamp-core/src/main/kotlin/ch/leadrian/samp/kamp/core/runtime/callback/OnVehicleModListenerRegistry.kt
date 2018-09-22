package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehicleModListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehicleModListener>(OnVehicleModListener::class), OnVehicleModListener {

    override fun onVehicleMod(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, componentModel: ch.leadrian.samp.kamp.core.api.constants.VehicleComponentModel): ch.leadrian.samp.kamp.core.api.callback.OnVehicleModListener.Result {
        getListeners().forEach {
            it.onVehicleMod(player, vehicle, componentModel)
        }
    }

}
