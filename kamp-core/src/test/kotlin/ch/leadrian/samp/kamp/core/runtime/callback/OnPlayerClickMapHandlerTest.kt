package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener.Result.Continue
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickMapListener.Result.Processed
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerClickMapHandlerTest {

    private val onPlayerClickMapHandler = OnPlayerClickMapHandler()
    private val player = mockk<Player>()
    private val coordinates = vector3DOf(1f, 2f, 3f)

    @Test
    fun givenNoListenerItShouldReturnContinueResult() {
        val result = onPlayerClickMapHandler.onPlayerClickMap(player, coordinates)

        assertThat(result)
                .isEqualTo(Continue)
    }

    @Test
    fun givenAllListenersReturnContinueItShouldReturnContinue() {
        val listener1 = mockk<OnPlayerClickMapListener> {
            every { onPlayerClickMap(player, coordinates) } returns Continue
        }
        val listener2 = mockk<OnPlayerClickMapListener> {
            every { onPlayerClickMap(player, coordinates) } returns Continue
        }
        val listener3 = mockk<OnPlayerClickMapListener> {
            every { onPlayerClickMap(player, coordinates) } returns Continue
        }
        val onPlayerClickMapHandler = OnPlayerClickMapHandler()
        onPlayerClickMapHandler.register(listener1)
        onPlayerClickMapHandler.register(listener2)
        onPlayerClickMapHandler.register(listener3)

        val result = onPlayerClickMapHandler.onPlayerClickMap(player, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerClickMap(player, coordinates)
            listener2.onPlayerClickMap(player, coordinates)
            listener3.onPlayerClickMap(player, coordinates)
        }
        assertThat(result)
                .isEqualTo(Continue)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnPlayerClickMapListener> {
            every { onPlayerClickMap(player, coordinates) } returns Continue
        }
        val listener2 = mockk<OnPlayerClickMapListener> {
            every { onPlayerClickMap(player, coordinates) } returns Processed
        }
        val listener3 = mockk<OnPlayerClickMapListener>()
        val onPlayerClickMapHandler = OnPlayerClickMapHandler()
        onPlayerClickMapHandler.register(listener1)
        onPlayerClickMapHandler.register(listener2)
        onPlayerClickMapHandler.register(listener3)

        val result = onPlayerClickMapHandler.onPlayerClickMap(player, coordinates)

        verify(exactly = 1) {
            listener1.onPlayerClickMap(player, coordinates)
            listener2.onPlayerClickMap(player, coordinates)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Processed)
    }

}