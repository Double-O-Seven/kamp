package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnRconLoginAttemptListener
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class OnRconLoginAttemptHandlerTest {

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun shouldCallAllListeners(success: Boolean) {
        val listener1 = mockk<OnRconLoginAttemptListener>(relaxed = true)
        val listener2 = mockk<OnRconLoginAttemptListener>(relaxed = true)
        val listener3 = mockk<OnRconLoginAttemptListener>(relaxed = true)
        val onRconLoginAttemptHandler = OnRconLoginAttemptHandler()
        onRconLoginAttemptHandler.register(listener1)
        onRconLoginAttemptHandler.register(listener2)
        onRconLoginAttemptHandler.register(listener3)

        onRconLoginAttemptHandler.onRconLoginAttempt(ipAddress = "127.0.0.1", password = "hahaha", success = success)

        verify(exactly = 1) {
            listener1.onRconLoginAttempt(ipAddress = "127.0.0.1", password = "hahaha", success = success)
            listener2.onRconLoginAttempt(ipAddress = "127.0.0.1", password = "hahaha", success = success)
            listener3.onRconLoginAttempt(ipAddress = "127.0.0.1", password = "hahaha", success = success)
        }
    }

}