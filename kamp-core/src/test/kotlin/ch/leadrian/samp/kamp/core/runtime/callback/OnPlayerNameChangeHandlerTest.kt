package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerNameChangeListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerNameChangeHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerNameChangeListener>(relaxed = true)
        val listener2 = mockk<OnPlayerNameChangeListener>(relaxed = true)
        val listener3 = mockk<OnPlayerNameChangeListener>(relaxed = true)
        val player = mockk<Player>()
        val onPlayerNameChangeHandler = OnPlayerNameChangeHandler()
        onPlayerNameChangeHandler.register(listener1)
        onPlayerNameChangeHandler.register(listener2)
        onPlayerNameChangeHandler.register(listener3)

        onPlayerNameChangeHandler.onPlayerNameChange(player, "hans.wurst", "John_Sausage")

        verify(exactly = 1) {
            listener1.onPlayerNameChange(player, "hans.wurst", "John_Sausage")
            listener2.onPlayerNameChange(player, "hans.wurst", "John_Sausage")
            listener3.onPlayerNameChange(player, "hans.wurst", "John_Sausage")
        }
    }

}