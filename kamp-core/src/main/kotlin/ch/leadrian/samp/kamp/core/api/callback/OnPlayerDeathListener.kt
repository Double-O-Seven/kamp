package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerDeathListener {

    fun onPlayerDeath(player: Player, killer: Player?, reason: WeaponModel)

}
