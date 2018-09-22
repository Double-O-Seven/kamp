package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.BodyPart
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerGiveDamageActorListener {

    fun onPlayerGiveDamageActor(player: Player, actor: Actor, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart)

}
