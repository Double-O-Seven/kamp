package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.Vehicle

interface InterceptableVehicle : Vehicle {

    fun onSpawn()

    fun onDeath(killer: Player?)

    fun onEnter(player: Player, isPassenger: Boolean)

    fun onExit(player: Player)

}