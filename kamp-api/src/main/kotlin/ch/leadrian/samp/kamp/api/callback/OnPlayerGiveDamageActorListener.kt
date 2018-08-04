package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.BodyPart
import ch.leadrian.samp.kamp.api.constants.WeaponModel
import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerGiveDamageActorListener {

    fun onPlayerGiveDamageActor(player: Player, actor: Actor, amount: Float, weaponModel: WeaponModel, bodyPart: BodyPart): Boolean

}
