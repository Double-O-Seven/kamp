package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamInListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerStreamInHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerStreamInListener>(relaxed = true)
        val listener2 = mockk<OnPlayerStreamInListener>(relaxed = true)
        val listener3 = mockk<OnPlayerStreamInListener>(relaxed = true)
        val player = mockk<Player>()
        val forPlayer = mockk<Player>()
        val onPlayerStreamInHandler = OnPlayerStreamInHandler()
        onPlayerStreamInHandler.register(listener1)
        onPlayerStreamInHandler.register(listener2)
        onPlayerStreamInHandler.register(listener3)

        onPlayerStreamInHandler.onPlayerStreamIn(player = player, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onPlayerStreamIn(player = player, forPlayer = forPlayer)
            listener2.onPlayerStreamIn(player = player, forPlayer = forPlayer)
            listener3.onPlayerStreamIn(player = player, forPlayer = forPlayer)
        }
    }

}