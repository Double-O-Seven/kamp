package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VehicleCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnVehicleSpawnListener, OnVehicleDeathListener, OnPlayerEnterVehicleListener, OnPlayerExitVehicleListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onVehicleSpawn(vehicle: Vehicle) {
        vehicle.onSpawn()
    }

    override fun onVehicleDeath(vehicle: Vehicle, killer: Player?) {
        vehicle.onDeath(killer)
    }

    override fun onPlayerEnterVehicle(player: Player, vehicle: Vehicle, isPassenger: Boolean) {
        vehicle.onEnter(player, isPassenger)
    }

    override fun onPlayerExitVehicle(player: Player, vehicle: Vehicle) {
        vehicle.onExit(player)
    }
}