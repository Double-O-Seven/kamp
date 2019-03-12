package ch.leadrian.samp.kamp.core.api.exception

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.Logger

internal class SafeCallerTest {

    @Test
    fun givenNoExceptionItShouldNotCallUncaughtExceptionNotifier() {
        val uncaughtExceptionNotifier = mockk<UncaughtExceptionNotifier>()
        val log = mockk<Logger>()
        val safeCaller = TestSafeCaller(uncaughtExceptionNotifier, log)

        val result = safeCaller.tryAndCatch { 1337 }

        assertThat(result)
                .isEqualTo(1337)
    }

    @Test
    fun givenExceptionAndUncaughtExceptionNotifierItShouldCallUncaughtExceptionNotifierAndLogException() {
        val exception = Exception("Throw me a bone")
        val uncaughtExceptionNotifier = mockk<UncaughtExceptionNotifier> {
            every { notify(any()) } just Runs
        }
        val log = mockk<Logger> {
            every { error(any<String>(), any()) } just Runs
        }
        val safeCaller = TestSafeCaller(uncaughtExceptionNotifier, log)

        safeCaller.tryAndCatch {
            throw exception
        }

        verifyOrder {
            log.error("Uncaught exception", exception)
            uncaughtExceptionNotifier.notify(exception)
        }
    }

    @Test
    fun givenUncaughtExceptionNotifierThrowsAnExceptionItShouldCatchExceptionAndLogIt() {
        val exception1 = Exception("Throw me a bone")
        val exception2 = Exception("Exception while notifying")
        val uncaughtExceptionNotifier = mockk<UncaughtExceptionNotifier> {
            every { notify(any()) } throws exception2
        }
        val log = mockk<Logger> {
            every { error(any<String>(), any()) } just Runs
            every { error(any<String>(), any(), any()) } just Runs
        }
        val safeCaller = TestSafeCaller(uncaughtExceptionNotifier, log)

        safeCaller.tryAndCatch {
            throw exception1
        }

        verify {
            log.error("Exception while notifying {} about exception", uncaughtExceptionNotifier, exception2)
        }
    }

    @Test
    fun givenExceptionAndAndNoUncaughtExceptionNotifierItShouldCatchExceptionAndLogException() {
        val exception = Exception("Throw me a bone")
        val log = mockk<Logger> {
            every { error(any<String>(), any()) } just Runs
        }
        val safeCaller = TestSafeCaller(null, log)

        safeCaller.tryAndCatch {
            throw exception
        }

        verify {
            log.error("Uncaught exception", exception)
        }
    }

    private class TestSafeCaller(
            override val uncaughtExceptionNotifier: UncaughtExceptionNotifier?,
            override val log: Logger
    ) : SafeCaller

}