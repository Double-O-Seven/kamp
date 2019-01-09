package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TextDrawCallbackListener
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerClickTextDrawListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerClickTextDraw(
            player: Player,
            textDraw: TextDraw
    ): OnPlayerClickTextDrawListener.Result = textDraw.onClick(player)

}