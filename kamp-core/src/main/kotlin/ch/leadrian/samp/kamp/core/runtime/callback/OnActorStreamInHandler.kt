package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamInListener
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnActorStreamInHandler
@Inject
constructor() : CallbackListenerRegistry<OnActorStreamInListener>(OnActorStreamInListener::class), OnActorStreamInListener {

    override fun onActorStreamIn(actor: Actor, forPlayer: Player) {
        listeners.forEach {
            it.onActorStreamIn(actor, forPlayer)
        }
    }

}
