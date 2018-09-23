package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnActorStreamOutHandler
@Inject
constructor() : CallbackListenerRegistry<OnActorStreamOutListener>(OnActorStreamOutListener::class), OnActorStreamOutListener {

    override fun onActorStreamOut(actor: Actor, forPlayer: Player) {
        listeners.forEach {
            it.onActorStreamOut(actor, forPlayer)
        }
    }

}
