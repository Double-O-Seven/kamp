package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStreamOutListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerStreamOutHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerStreamOutListener>(relaxed = true)
        val listener2 = mockk<OnPlayerStreamOutListener>(relaxed = true)
        val listener3 = mockk<OnPlayerStreamOutListener>(relaxed = true)
        val player = mockk<Player>()
        val forPlayer = mockk<Player>()
        val onPlayerStreamOutHandler = OnPlayerStreamOutHandler()
        onPlayerStreamOutHandler.register(listener1)
        onPlayerStreamOutHandler.register(listener2)
        onPlayerStreamOutHandler.register(listener3)

        onPlayerStreamOutHandler.onPlayerStreamOut(player = player, forPlayer = forPlayer)

        verify(exactly = 1) {
            listener1.onPlayerStreamOut(player = player, forPlayer = forPlayer)
            listener2.onPlayerStreamOut(player = player, forPlayer = forPlayer)
            listener3.onPlayerStreamOut(player = player, forPlayer = forPlayer)
        }
    }

}