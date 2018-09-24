package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerRequestSpawnListener.Result
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerRequestSpawnHandlerTest {

    private val onPlayerRequestSpawnHandler = OnPlayerRequestSpawnHandler()
    private val player = mockk<Player>()

    @Test
    fun givenNoListenerItShouldReturnGrantedResult() {
        val result = onPlayerRequestSpawnHandler.onPlayerRequestSpawn(player)

        assertThat(result)
                .isEqualTo(Result.Granted)
    }

    @Test
    fun givenAllListenersReturnGrantedItShouldReturnGranted() {
        val listener1 = mockk<OnPlayerRequestSpawnListener> {
            every {
                onPlayerRequestSpawn(player)
            } returns Result.Granted
        }
        val listener2 = mockk<OnPlayerRequestSpawnListener> {
            every {
                onPlayerRequestSpawn(player)
            } returns Result.Granted
        }
        val listener3 = mockk<OnPlayerRequestSpawnListener> {
            every {
                onPlayerRequestSpawn(player)
            } returns Result.Granted
        }
        val onPlayerRequestSpawnHandler = OnPlayerRequestSpawnHandler()
        onPlayerRequestSpawnHandler.register(listener1)
        onPlayerRequestSpawnHandler.register(listener2)
        onPlayerRequestSpawnHandler.register(listener3)

        val result = onPlayerRequestSpawnHandler.onPlayerRequestSpawn(player)

        verify(exactly = 1) {
            listener1.onPlayerRequestSpawn(player)
            listener2.onPlayerRequestSpawn(player)
            listener3.onPlayerRequestSpawn(player)
        }
        assertThat(result)
                .isEqualTo(Result.Granted)
    }

    @Test
    fun shouldStopWithFirstDeniedResult() {
        val listener1 = mockk<OnPlayerRequestSpawnListener> {
            every { onPlayerRequestSpawn(player) } returns Result.Granted
        }
        val listener2 = mockk<OnPlayerRequestSpawnListener> {
            every { onPlayerRequestSpawn(player) } returns Result.Denied
        }
        val listener3 = mockk<OnPlayerRequestSpawnListener>()
        val onPlayerRequestSpawnHandler = OnPlayerRequestSpawnHandler()
        onPlayerRequestSpawnHandler.register(listener1)
        onPlayerRequestSpawnHandler.register(listener2)
        onPlayerRequestSpawnHandler.register(listener3)

        val result = onPlayerRequestSpawnHandler.onPlayerRequestSpawn(player)

        verify(exactly = 1) {
            listener1.onPlayerRequestSpawn(player)
            listener2.onPlayerRequestSpawn(player)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Result.Denied)
    }

}