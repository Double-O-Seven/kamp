package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener.Result
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickMapListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickMapListener>(OnPlayerClickMapListener::class), OnPlayerClickMapListener {

    override fun onPlayerClickMap(player: Player, coordinates: Vector3D): Result {
        return listeners.map {
            it.onPlayerClickMap(player, coordinates)
        }.firstOrNull { it == Result.Processed } ?: Result.Continue
    }

}
