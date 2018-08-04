package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.BodyPart
import ch.leadrian.samp.kamp.api.constants.WeaponModel
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerTakeDamageListener {

    fun onPlayerTakeDamage(player: Player, issuer: Player?, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart): Boolean

}
