package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.WeaponModel
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerDeathListener {

    fun onPlayerDeath(player: Player, killer: Player?, reason: WeaponModel): Boolean

}
