package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnIncomingConnectionListener
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnIncomingConnectionListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnIncomingConnectionListener>(OnIncomingConnectionListener::class), OnIncomingConnectionListener {

    override fun onIncomingConnection(playerId: PlayerId, ipAddress: String, port: Int) {
        listeners.forEach {
            it.onIncomingConnection(playerId, ipAddress, port)
        }
    }

}
