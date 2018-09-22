package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnIncomingConnectionListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnIncomingConnectionListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnIncomingConnectionListener>(OnIncomingConnectionListener::class), OnIncomingConnectionListener {

    override fun onIncomingConnection(playerId: ch.leadrian.samp.kamp.core.api.entity.id.PlayerId, ipAddress: kotlin.String, port: kotlin.Int): kotlin.Boolean {
        getListeners().forEach {
            it.onIncomingConnection(playerId, ipAddress, port)
        }
    }

}
