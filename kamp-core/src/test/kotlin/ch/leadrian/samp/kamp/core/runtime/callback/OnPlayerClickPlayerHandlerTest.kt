package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener.Result.Continue
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerListener.Result.Processed
import ch.leadrian.samp.kamp.core.api.constants.ClickPlayerSource
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnPlayerClickPlayerHandlerTest {

    private val onPlayerClickPlayerHandler = OnPlayerClickPlayerHandler()
    private val player = mockk<Player>()
    private val clickedPlayer = mockk<Player>()
    private val source = ClickPlayerSource.SCOREBOARD

    @Test
    fun givenNoListenerItShouldReturnContinueResult() {
        val result = onPlayerClickPlayerHandler.onPlayerClickPlayer(
                player = player,
                clickedPlayer = clickedPlayer,
                source = source
        )

        assertThat(result)
                .isEqualTo(Continue)
    }

    @Test
    fun givenAllListenersReturnContinueItShouldReturnContinue() {
        val listener1 = mockk<OnPlayerClickPlayerListener> {
            every {
                onPlayerClickPlayer(
                        player = player,
                        clickedPlayer = clickedPlayer,
                        source = source
                )
            } returns Continue
        }
        val listener2 = mockk<OnPlayerClickPlayerListener> {
            every {
                onPlayerClickPlayer(
                        player = player,
                        clickedPlayer = clickedPlayer,
                        source = source
                )
            } returns Continue
        }
        val listener3 = mockk<OnPlayerClickPlayerListener> {
            every {
                onPlayerClickPlayer(
                        player = player,
                        clickedPlayer = clickedPlayer,
                        source = source
                )
            } returns Continue
        }
        val onPlayerClickPlayerHandler = OnPlayerClickPlayerHandler()
        onPlayerClickPlayerHandler.register(listener1)
        onPlayerClickPlayerHandler.register(listener2)
        onPlayerClickPlayerHandler.register(listener3)

        val result = onPlayerClickPlayerHandler.onPlayerClickPlayer(
                player = player,
                clickedPlayer = clickedPlayer,
                source = source
        )

        verify(exactly = 1) {
            listener1.onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source)
            listener2.onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source)
            listener3.onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source)
        }
        assertThat(result)
                .isEqualTo(Continue)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnPlayerClickPlayerListener> {
            every { onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source) } returns Continue
        }
        val listener2 = mockk<OnPlayerClickPlayerListener> {
            every { onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source) } returns Processed
        }
        val listener3 = mockk<OnPlayerClickPlayerListener>()
        val onPlayerClickPlayerHandler = OnPlayerClickPlayerHandler()
        onPlayerClickPlayerHandler.register(listener1)
        onPlayerClickPlayerHandler.register(listener2)
        onPlayerClickPlayerHandler.register(listener3)

        val result = onPlayerClickPlayerHandler.onPlayerClickPlayer(
                player = player,
                clickedPlayer = clickedPlayer,
                source = source
        )

        verify(exactly = 1) {
            listener1.onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source)
            listener2.onPlayerClickPlayer(player = player, clickedPlayer = clickedPlayer, source = source)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(Processed)
    }

}