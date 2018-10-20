package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamInListener
import ch.leadrian.samp.kamp.core.api.callback.OnActorStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ActorCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnActorStreamInListener, OnActorStreamOutListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onActorStreamIn(actor: Actor, forPlayer: Player) {
        actor.onStreamIn(forPlayer)
    }

    override fun onActorStreamOut(actor: Actor, forPlayer: Player) {
        actor.onStreamOut(forPlayer)
    }
}