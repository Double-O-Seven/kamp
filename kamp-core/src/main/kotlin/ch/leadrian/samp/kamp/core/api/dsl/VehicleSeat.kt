package ch.leadrian.samp.kamp.core.api.dsl

import ch.leadrian.samp.kamp.core.api.entity.HasVehicle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.Vehicle

data class VehicleSeat(val seatId: Int, override val vehicle: Vehicle) :
        HasVehicle {

    @Suppress("unused")
    companion object {

        const val DRIVER: Int = 0

        const val FRONT_PASSENGER: Int = 1

        const val REAR_PASSENGER_1: Int = 2

        const val REAR_PASSENGER_2: Int = 3

    }

    operator fun contains(player: Player): Boolean {
        return player.vehicleSeat == seatId && player in vehicle
    }

}

infix fun Vehicle.seat(seatId: Int): VehicleSeat = VehicleSeat(seatId, this)

infix fun Player.isDriverOf(vehicle: Vehicle): Boolean = vehicleSeat == VehicleSeat.DRIVER && this in vehicle

infix fun Player.isPassengerOf(vehicle: Vehicle): Boolean = vehicleSeat != VehicleSeat.DRIVER && this in vehicle
