package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnIncomingConnectionListener
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnIncomingConnectionHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnIncomingConnectionListener>(relaxed = true)
        val listener2 = mockk<OnIncomingConnectionListener>(relaxed = true)
        val listener3 = mockk<OnIncomingConnectionListener>(relaxed = true)
        val onIncomingConnectionHandler = OnIncomingConnectionHandler()
        onIncomingConnectionHandler.register(listener1)
        onIncomingConnectionHandler.register(listener2)
        onIncomingConnectionHandler.register(listener3)

        onIncomingConnectionHandler.onIncomingConnection(playerId = PlayerId.valueOf(69), ipAddress = "127.0.0.1", port = 7777)

        verify(exactly = 1) {
            listener1.onIncomingConnection(playerId = PlayerId.valueOf(69), ipAddress = "127.0.0.1", port = 7777)
            listener2.onIncomingConnection(playerId = PlayerId.valueOf(69), ipAddress = "127.0.0.1", port = 7777)
            listener3.onIncomingConnection(playerId = PlayerId.valueOf(69), ipAddress = "127.0.0.1", port = 7777)
        }
    }

}