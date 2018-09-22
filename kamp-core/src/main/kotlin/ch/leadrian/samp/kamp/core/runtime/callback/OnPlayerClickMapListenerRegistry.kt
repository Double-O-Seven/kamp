package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerClickMapListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerClickMapListener>(OnPlayerClickMapListener::class), OnPlayerClickMapListener {

    override fun onPlayerClickMap(player: ch.leadrian.samp.kamp.core.api.entity.Player, coordinates: ch.leadrian.samp.kamp.core.api.data.Vector3D): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerClickMap(player, coordinates)
        }
    }

}
