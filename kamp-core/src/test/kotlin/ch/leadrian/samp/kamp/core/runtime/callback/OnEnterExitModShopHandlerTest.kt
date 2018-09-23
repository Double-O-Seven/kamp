package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnEnterExitModShopListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnEnterExitModShopHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnEnterExitModShopListener>(relaxed = true)
        val listener2 = mockk<OnEnterExitModShopListener>(relaxed = true)
        val listener3 = mockk<OnEnterExitModShopListener>(relaxed = true)
        val forPlayer = mockk<Player>()
        val onEnterExitModShopHandler = OnEnterExitModShopHandler()
        onEnterExitModShopHandler.register(listener1)
        onEnterExitModShopHandler.register(listener2)
        onEnterExitModShopHandler.register(listener3)

        onEnterExitModShopHandler.onEnterExitModShop(player = forPlayer, entered = true, interiorId = 1337)

        verify(exactly = 1) {
            listener1.onEnterExitModShop(player = forPlayer, entered = true, interiorId = 1337)
            listener2.onEnterExitModShop(player = forPlayer, entered = true, interiorId = 1337)
            listener3.onEnterExitModShop(player = forPlayer, entered = true, interiorId = 1337)
        }
    }

}