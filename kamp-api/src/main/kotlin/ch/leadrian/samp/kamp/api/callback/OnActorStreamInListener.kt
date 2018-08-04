package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.Player

interface OnActorStreamInListener {

    fun onActorStreamIn(actor: Actor, forPlayer: Player)

}
