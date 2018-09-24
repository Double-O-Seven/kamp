package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerConnectHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerConnectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerConnectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerConnectListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerConnectHandler = OnPlayerConnectHandler()
        onPlayerConnectHandler.register(listener1)
        onPlayerConnectHandler.register(listener2)
        onPlayerConnectHandler.register(listener3)

        onPlayerConnectHandler.onPlayerConnect(player)

        verify(exactly = 1) {
            listener1.onPlayerConnect(player)
            listener2.onPlayerConnect(player)
            listener3.onPlayerConnect(player)
        }
    }

}