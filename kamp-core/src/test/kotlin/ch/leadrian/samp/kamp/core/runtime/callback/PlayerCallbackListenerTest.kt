package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerCallbackListenerTest {

    private lateinit var playerCallbackListener: PlayerCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        playerCallbackListener = PlayerCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        playerCallbackListener.initialize()

        verify { callbackListenerManager.register(playerCallbackListener) }
    }

    @Test
    fun shouldCallOnSpawn() {
        val player = mockk<Player> {
            every { onSpawn() } just Runs
        }

        playerCallbackListener.onPlayerSpawn(player)

        verify { player.onSpawn() }
    }

    @Test
    fun shouldCallOnDeath() {
        val player = mockk<Player> {
            every { onDeath(any(), any()) } just Runs
        }
        val killer = mockk<Player>()

        playerCallbackListener.onPlayerDeath(player, killer, WeaponModel.TEC9)

        verify { player.onDeath(killer, WeaponModel.TEC9) }
    }

    @Test
    fun shouldCallOnDisconnect() {
        val player = mockk<Player> {
            every { onDisconnect(any<DisconnectReason>()) } just Runs
        }

        playerCallbackListener.onPlayerDisconnect(player, DisconnectReason.QUIT)

        verify { player.onDisconnect(DisconnectReason.QUIT) }
    }

}