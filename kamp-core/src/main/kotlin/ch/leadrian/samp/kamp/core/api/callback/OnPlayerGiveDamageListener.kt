package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerGiveDamageListener {

    fun onPlayerGiveDamage(player: Player, damagedPlayer: Player, amount: Float, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, bodyPart: ch.leadrian.samp.kamp.core.api.constants.BodyPart): Boolean

}
