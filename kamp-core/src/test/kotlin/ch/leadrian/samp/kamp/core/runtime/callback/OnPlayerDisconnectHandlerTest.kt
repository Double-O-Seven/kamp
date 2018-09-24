package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class OnPlayerDisconnectHandlerTest {

    @ParameterizedTest
    @EnumSource(DisconnectReason::class)
    fun shouldCallAllListeners(reason: DisconnectReason) {
        val listener1 = mockk<OnPlayerDisconnectListener>(relaxed = true)
        val listener2 = mockk<OnPlayerDisconnectListener>(relaxed = true)
        val listener3 = mockk<OnPlayerDisconnectListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerDisconnectHandler = OnPlayerDisconnectHandler()
        onPlayerDisconnectHandler.register(listener1)
        onPlayerDisconnectHandler.register(listener2)
        onPlayerDisconnectHandler.register(listener3)

        onPlayerDisconnectHandler.onPlayerDisconnect(player, reason)

        verify(exactly = 1) {
            listener1.onPlayerDisconnect(player, reason)
            listener2.onPlayerDisconnect(player, reason)
            listener3.onPlayerDisconnect(player, reason)
        }
    }

}