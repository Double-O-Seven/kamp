package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerEnterVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitVehicleListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleDeathListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehiclePaintjobListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleResprayListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnVehicleStreamOutListener
import ch.leadrian.samp.kamp.core.api.callback.Priority
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Priority(Int.MIN_VALUE, OnVehicleResprayListener::class)
@Singleton
internal class VehicleCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnVehicleSpawnListener,
        OnVehicleDeathListener,
        OnPlayerEnterVehicleListener,
        OnPlayerExitVehicleListener,
        OnVehicleStreamInListener,
        OnVehicleStreamOutListener,
        OnVehicleResprayListener,
        OnVehiclePaintjobListener {

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

    override fun onVehicleStreamIn(vehicle: Vehicle, forPlayer: Player) {
        vehicle.onStreamIn(forPlayer)
    }

    override fun onVehicleStreamOut(vehicle: Vehicle, forPlayer: Player) {
        vehicle.onStreamOut(forPlayer)
    }

    override fun onVehicleRespray(
            player: Player,
            vehicle: Vehicle,
            colors: VehicleColors
    ): OnVehicleResprayListener.Result = vehicle.onRespray(player, colors)

    override fun onVehiclePaintjob(player: Player, vehicle: Vehicle, paintjobId: Int) {
        vehicle.onPaintjobChange(player, paintjobId)
    }
}