package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnActorStreamOutListener {

    fun onActorStreamOut(actor: Actor, forPlayer: Player)

}
