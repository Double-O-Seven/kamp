package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerTakeDamageListener {

    fun onPlayerTakeDamage(player: Player, issuer: Player?, amount: Float, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, bodyPart: ch.leadrian.samp.kamp.core.api.constants.BodyPart): Boolean

}
