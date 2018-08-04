package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.Player

interface OnActorStreamOutListener {

    fun onActorStreamOut(actor: Actor, forPlayer: Player)

}
