package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDeathListener
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerDeathHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerDeathListener>(relaxed = true)
        val listener2 = mockk<OnPlayerDeathListener>(relaxed = true)
        val listener3 = mockk<OnPlayerDeathListener>(relaxed = true)
        val player = mockk<Player>()
        val killer = mockk<Player>()
        val onPlayerDeathHandler = OnPlayerDeathHandler()
        onPlayerDeathHandler.register(listener1)
        onPlayerDeathHandler.register(listener2)
        onPlayerDeathHandler.register(listener3)

        onPlayerDeathHandler.onPlayerDeath(player = player, killer = killer, reason = WeaponModel.AK47)

        verify(exactly = 1) {
            listener1.onPlayerDeath(player = player, killer = killer, reason = WeaponModel.AK47)
            listener2.onPlayerDeath(player = player, killer = killer, reason = WeaponModel.AK47)
            listener3.onPlayerDeath(player = player, killer = killer, reason = WeaponModel.AK47)
        }
    }

    @Test
    fun givenNoKillerItShouldCallAllListeners() {
        val listener1 = mockk<OnPlayerDeathListener>(relaxed = true)
        val listener2 = mockk<OnPlayerDeathListener>(relaxed = true)
        val listener3 = mockk<OnPlayerDeathListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerDeathHandler = OnPlayerDeathHandler()
        onPlayerDeathHandler.register(listener1)
        onPlayerDeathHandler.register(listener2)
        onPlayerDeathHandler.register(listener3)

        onPlayerDeathHandler.onPlayerDeath(player = player, killer = null, reason = WeaponModel.AK47)

        verify(exactly = 1) {
            listener1.onPlayerDeath(player = player, killer = null, reason = WeaponModel.AK47)
            listener2.onPlayerDeath(player = player, killer = null, reason = WeaponModel.AK47)
            listener3.onPlayerDeath(player = player, killer = null, reason = WeaponModel.AK47)
        }
    }

}