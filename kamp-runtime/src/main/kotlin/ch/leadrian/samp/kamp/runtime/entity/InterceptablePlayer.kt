package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.api.constants.WeaponModel
import ch.leadrian.samp.kamp.api.entity.Player

interface InterceptablePlayer : Player {

    fun onDisconnect(reason: DisconnectReason)

    fun onSpawn()

    fun onDeath(killer: Player?, weapon: WeaponModel)

}