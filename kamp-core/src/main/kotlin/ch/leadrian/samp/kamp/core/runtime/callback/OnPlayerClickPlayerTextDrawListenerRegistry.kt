package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickPlayerTextDrawListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickPlayerTextDrawListener>(OnPlayerClickPlayerTextDrawListener::class), OnPlayerClickPlayerTextDrawListener {

    override fun onPlayerClickPlayerTextDraw(textDraw: ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw): ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener.Result {
        getListeners().forEach {
            it.onPlayerClickPlayerTextDraw(textDraw)
        }
    }

}
