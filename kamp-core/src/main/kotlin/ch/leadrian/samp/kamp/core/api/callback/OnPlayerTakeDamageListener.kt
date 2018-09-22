package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerTakeDamageListener {

    fun onPlayerTakeDamage(player: Player, issuer: Player?, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart)

}
