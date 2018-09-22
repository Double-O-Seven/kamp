package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener.Result
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickPlayerTextDrawListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickPlayerTextDrawListener>(OnPlayerClickPlayerTextDrawListener::class), OnPlayerClickPlayerTextDrawListener {

    override fun onPlayerClickPlayerTextDraw(textDraw: PlayerTextDraw): Result {
        return listeners.map {
            it.onPlayerClickPlayerTextDraw(textDraw)
        }.firstOrNull { it == Result.Processed } ?: Result.NotFound
    }

}
