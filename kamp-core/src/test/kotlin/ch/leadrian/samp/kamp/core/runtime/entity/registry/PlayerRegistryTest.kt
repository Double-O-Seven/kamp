package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PlayerRegistryTest {

    private lateinit var playerRegistry: PlayerRegistry
    private lateinit var nativeFunctionExecutor: SAMPNativeFunctionExecutor
    private val callbackListenerManager = mockk<CallbackListenerManager>()

    @BeforeEach
    fun setUp() {
        nativeFunctionExecutor = mockk {
            every { getMaxPlayers() } returns 69
        }
        playerRegistry = PlayerRegistry(nativeFunctionExecutor, callbackListenerManager)
    }

    @Test
    fun initializeShouldCallCallbackListenerManager() {
        every { callbackListenerManager.register(any()) } just Runs

        playerRegistry.initialize()

        verify { callbackListenerManager.register(playerRegistry) }
    }

    @Test
    fun shouldHaveExpectedCapacity() {
        val capacity = playerRegistry.capacity

        assertThat(capacity)
                .isEqualTo(69)
    }

    @Test
    fun onPlayerDisconnectShouldUnregisterPlayer() {
        val playerId = PlayerId.valueOf(50)
        val player = mockk<Player> {
            every { id } returns playerId
        }
        playerRegistry.register(player)

        playerRegistry.onPlayerDisconnect(player, DisconnectReason.QUIT)

        assertThat(playerRegistry[playerId])
                .isNull()
    }

}