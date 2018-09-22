package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerConnectListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerConnectListener>(OnPlayerConnectListener::class), OnPlayerConnectListener {

    override fun onPlayerConnect(player: ch.leadrian.samp.kamp.core.api.entity.Player): kotlin.Boolean {
        getListeners().forEach {
            it.onPlayerConnect(player)
        }
    }

}
