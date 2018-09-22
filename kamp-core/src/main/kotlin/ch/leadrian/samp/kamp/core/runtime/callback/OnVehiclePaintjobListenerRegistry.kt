package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnVehiclePaintjobListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnVehiclePaintjobListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnVehiclePaintjobListener>(OnVehiclePaintjobListener::class), OnVehiclePaintjobListener {

    override fun onVehiclePaintjob(player: ch.leadrian.samp.kamp.core.api.entity.Player, vehicle: ch.leadrian.samp.kamp.core.api.entity.Vehicle, paintjobId: kotlin.Int): kotlin.Boolean {
        getListeners().forEach {
            it.onVehiclePaintjob(player, vehicle, paintjobId)
        }
    }

}
