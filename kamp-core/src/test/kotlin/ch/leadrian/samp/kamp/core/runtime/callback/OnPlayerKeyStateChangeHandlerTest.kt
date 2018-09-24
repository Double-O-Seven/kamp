package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerKeyStateChangeListener
import ch.leadrian.samp.kamp.core.api.entity.PlayerKeys
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnPlayerKeyStateChangeHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnPlayerKeyStateChangeListener>(relaxed = true)
        val listener2 = mockk<OnPlayerKeyStateChangeListener>(relaxed = true)
        val listener3 = mockk<OnPlayerKeyStateChangeListener>(relaxed = true)
        val oldKeys = mockk<PlayerKeys>()
        val newKeys = mockk<PlayerKeys>()
        val onPlayerKeyStateChangeHandler = OnPlayerKeyStateChangeHandler()
        onPlayerKeyStateChangeHandler.register(listener1)
        onPlayerKeyStateChangeHandler.register(listener2)
        onPlayerKeyStateChangeHandler.register(listener3)

        onPlayerKeyStateChangeHandler.onPlayerKeyStateChange(oldKeys = oldKeys, newKeys = newKeys)

        verify(exactly = 1) {
            listener1.onPlayerKeyStateChange(oldKeys = oldKeys, newKeys = newKeys)
            listener2.onPlayerKeyStateChange(oldKeys = oldKeys, newKeys = newKeys)
            listener3.onPlayerKeyStateChange(oldKeys = oldKeys, newKeys = newKeys)
        }
    }

}