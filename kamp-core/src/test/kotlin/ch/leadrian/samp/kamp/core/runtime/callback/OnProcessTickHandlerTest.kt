package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnProcessTickListener
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OnProcessTickHandlerTest {

    @Test
    fun shouldCallAllListeners() {
        val listener1 = mockk<OnProcessTickListener>(relaxed = true)
        val listener2 = mockk<OnProcessTickListener>(relaxed = true)
        val listener3 = mockk<OnProcessTickListener>(relaxed = true)
        val onProcessTickHandler = OnProcessTickHandler()
        onProcessTickHandler.register(listener1)
        onProcessTickHandler.register(listener2)
        onProcessTickHandler.register(listener3)

        onProcessTickHandler.onProcessTick()

        verify(exactly = 1) {
            listener1.onProcessTick()
            listener2.onProcessTick()
            listener3.onProcessTick()
        }
    }

}