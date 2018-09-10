package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

interface OnUnoccupiedVehicleUpdateListener {

    fun onUnoccupiedVehicleUpdate(
            vehicle: Vehicle,
            player: Player,
            passengerSeat: Int?,
            coordinates: Vector3D,
            velocity: Vector3D
    ): Boolean

}
