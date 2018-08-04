package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface OnUnoccupiedVehicleUpdateListener {

    fun onUnoccupiedVehicleUpdate(
            vehicle: Vehicle,
            player: Player,
            passengerSeat: Int?,
            coordinates: Vector3D,
            velocity: Vector3D
    ): Boolean

}
