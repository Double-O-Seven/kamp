package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestClassListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerClass
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class OnPlayerRequestClassHandlerTest {

    private val onPlayerRequestClassHandler = OnPlayerRequestClassHandler()
    private val player = mockk<Player>()
    private val playerClass = mockk<PlayerClass>()

    @Test
    fun givenNoListenerItShouldReturnAllowResult() {
        val result = onPlayerRequestClassHandler.onPlayerRequestClass(player, playerClass)

        Assertions.assertThat(result)
                .isEqualTo(OnPlayerRequestClassListener.Result.Allow)
    }

    @Test
    fun givenAllListenersReturnAllowItShouldReturnAllow() {
        val listener1 = mockk<OnPlayerRequestClassListener> {
            every {
                onPlayerRequestClass(player, playerClass)
            } returns OnPlayerRequestClassListener.Result.Allow
        }
        val listener2 = mockk<OnPlayerRequestClassListener> {
            every {
                onPlayerRequestClass(player, playerClass)
            } returns OnPlayerRequestClassListener.Result.Allow
        }
        val listener3 = mockk<OnPlayerRequestClassListener> {
            every {
                onPlayerRequestClass(player, playerClass)
            } returns OnPlayerRequestClassListener.Result.Allow
        }
        val onPlayerRequestClassHandler = OnPlayerRequestClassHandler()
        onPlayerRequestClassHandler.register(listener1)
        onPlayerRequestClassHandler.register(listener2)
        onPlayerRequestClassHandler.register(listener3)

        val result = onPlayerRequestClassHandler.onPlayerRequestClass(player, playerClass)

        verify(exactly = 1) {
            listener1.onPlayerRequestClass(player, playerClass)
            listener2.onPlayerRequestClass(player, playerClass)
            listener3.onPlayerRequestClass(player, playerClass)
        }
        Assertions.assertThat(result)
                .isEqualTo(OnPlayerRequestClassListener.Result.Allow)
    }

    @Test
    fun shouldStopWithFirstDeniedResult() {
        val listener1 = mockk<OnPlayerRequestClassListener> {
            every { onPlayerRequestClass(player, playerClass) } returns OnPlayerRequestClassListener.Result.Allow
        }
        val listener2 = mockk<OnPlayerRequestClassListener> {
            every { onPlayerRequestClass(player, playerClass) } returns OnPlayerRequestClassListener.Result.PreventSpawn
        }
        val listener3 = mockk<OnPlayerRequestClassListener>()
        val onPlayerRequestClassHandler = OnPlayerRequestClassHandler()
        onPlayerRequestClassHandler.register(listener1)
        onPlayerRequestClassHandler.register(listener2)
        onPlayerRequestClassHandler.register(listener3)

        val result = onPlayerRequestClassHandler.onPlayerRequestClass(player, playerClass)

        verify(exactly = 1) {
            listener1.onPlayerRequestClass(player, playerClass)
            listener2.onPlayerRequestClass(player, playerClass)
        }
        verify { listener3 wasNot Called }
        Assertions.assertThat(result)
                .isEqualTo(OnPlayerRequestClassListener.Result.PreventSpawn)
    }

}