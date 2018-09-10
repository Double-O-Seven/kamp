package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerGiveDamageActorListener {

    fun onPlayerGiveDamageActor(player: Player, actor: Actor, amount: Float, weaponModel: ch.leadrian.samp.kamp.core.api.constants.WeaponModel, bodyPart: ch.leadrian.samp.kamp.core.api.constants.BodyPart): Boolean

}
