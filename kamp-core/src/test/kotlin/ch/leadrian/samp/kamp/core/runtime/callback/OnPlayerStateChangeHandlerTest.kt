package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerStateChangeListener
import ch.leadrian.samp.kamp.core.api.constants.PlayerState
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerStateChangeHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerStateChangeListener>(relaxed = true)
        val listener2 = mockk<OnPlayerStateChangeListener>(relaxed = true)
        val listener3 = mockk<OnPlayerStateChangeListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerStateChangeHandler = OnPlayerStateChangeHandler()
        onPlayerStateChangeHandler.register(listener1)
        onPlayerStateChangeHandler.register(listener2)
        onPlayerStateChangeHandler.register(listener3)

        onPlayerStateChangeHandler.onPlayerStateChange(
                player = player,
                newState = PlayerState.ON_FOOT,
                oldState = PlayerState.ENTER_VEHICLE_DRIVER
        )

        verify(exactly = 1) {
            listener1.onPlayerStateChange(
                    player = player,
                    newState = PlayerState.ON_FOOT,
                    oldState = PlayerState.ENTER_VEHICLE_DRIVER
            )
            listener2.onPlayerStateChange(
                    player = player,
                    newState = PlayerState.ON_FOOT,
                    oldState = PlayerState.ENTER_VEHICLE_DRIVER
            )
            listener3.onPlayerStateChange(
                    player = player,
                    newState = PlayerState.ON_FOOT,
                    oldState = PlayerState.ENTER_VEHICLE_DRIVER
            )
        }
    }

}