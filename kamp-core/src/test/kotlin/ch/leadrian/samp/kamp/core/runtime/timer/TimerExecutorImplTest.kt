package ch.leadrian.samp.kamp.core.runtime.timer

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.runtime.async.ExecutorServiceFactory
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

internal class TimerExecutorImplTest {

    private lateinit var timerExecutor: TimerExecutorImpl
    private val asyncExecutor = mockk<AsyncExecutor>()
    private val executorServiceFactory = mockk<ExecutorServiceFactory>()

    @BeforeEach
    fun setUp() {
        timerExecutor = TimerExecutorImpl(asyncExecutor, executorServiceFactory)
    }

    @Nested
    inner class ShutdownTests {

        private val scheduledExecutorService = mockk<ScheduledExecutorService>()

        @BeforeEach
        fun setUp() {
            every { executorServiceFactory.createSingleThreadScheduledExecutor() } returns scheduledExecutorService
        }

        @Test
        fun givenExecutorServiceWasNotUsedItShouldDoNothing() {
            timerExecutor.shutdown()

            verify { scheduledExecutorService wasNot Called }
        }

        @Test
        fun givenExecutorServiceWasUsedItShouldShutdownAndAwaitTermination() {
            every { scheduledExecutorService.shutdown() } just Runs
            every { scheduledExecutorService.awaitTermination(any(), any()) } returns true
            every { scheduledExecutorService.schedule(any(), any(), any()) } returns mockk()
            timerExecutor.addTimer(1000) { }

            timerExecutor.shutdown()

            verifyOrder {
                scheduledExecutorService.shutdown()
                scheduledExecutorService.awaitTermination(3, TimeUnit.MINUTES)
            }
        }

    }

    @Nested
    inner class AddTimerTests {

        private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        @BeforeEach
        fun setUp() {
            every { executorServiceFactory.createSingleThreadScheduledExecutor() } returns scheduledExecutorService
            every { asyncExecutor.executeOnMainThread(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
        }

        @AfterEach
        fun tearDown() {
            scheduledExecutorService.shutdown()
            scheduledExecutorService.awaitTermination(30, TimeUnit.SECONDS)
        }

        @Test
        fun shouldExecuteTimerOnce() {
            val action = mockk<() -> Unit>(relaxed = true)

            timerExecutor.addTimer(2000) {
                action()
            }

            Thread.sleep(5000)
            verify(exactly = 1) {
                action.invoke()
                asyncExecutor.executeOnMainThread(any())
            }
        }

        @Test
        fun stopShouldPreventExecution() {
            val action = mockk<() -> Unit>(relaxed = true)
            val timer = timerExecutor.addTimer(2000) {
                action()
            }
            Thread.sleep(1000)

            timer.stop()

            Thread.sleep(5000)
            verify(exactly = 0) {
                action.invoke()
                asyncExecutor.executeOnMainThread(any())
            }
        }

    }

    @Nested
    inner class AddRepeatingTimerTests {

        private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

        @BeforeEach
        fun setUp() {
            every { executorServiceFactory.createSingleThreadScheduledExecutor() } returns scheduledExecutorService
            every { asyncExecutor.executeOnMainThread(any()) } answers {
                firstArg<() -> Unit>().invoke()
            }
        }

        @AfterEach
        fun tearDown() {
            scheduledExecutorService.shutdown()
            scheduledExecutorService.awaitTermination(30, TimeUnit.SECONDS)
        }

        @Nested
        inner class FiniteNumberOfRepetitionsTests {

            @ParameterizedTest
            @ValueSource(ints = [0, 1, 3, 5])
            fun shouldExecuteTimerSpecifiedNumberOfTimes(maxRepetitions: Int) {
                val action = mockk<() -> Unit>(relaxed = true)

                timerExecutor.addRepeatingTimer(maxRepetitions, 1000) {
                    action()
                }

                Thread.sleep((maxRepetitions * 2000).toLong())
                verify(exactly = maxRepetitions) {
                    action.invoke()
                    asyncExecutor.executeOnMainThread(any())
                }
            }

            @ParameterizedTest
            @CsvSource(
                    "1, 3",
                    "2, 5",
                    "3, 5",
                    "5, 5"
            )
            fun stopShouldCancelExecution(expectedRepetitions: Int, maxRepetitions: Int) {
                val action = mockk<() -> Unit>(relaxed = true)
                val timer = timerExecutor.addRepeatingTimer(maxRepetitions, 2000) {
                    action()
                }
                Thread.sleep((expectedRepetitions * 2000 + 1000).toLong())

                timer.stop()

                Thread.sleep((maxRepetitions - expectedRepetitions).toLong() * 2000 + 1000)
                verify(exactly = expectedRepetitions) {
                    action.invoke()
                    asyncExecutor.executeOnMainThread(any())
                }
            }
        }

        @Nested
        inner class InfiniteNumberOfRepetitionsTests {

            @ParameterizedTest
            @ValueSource(ints = [0, 1, 3, 5])
            fun shouldExecuteTimerSpecifiedNumberOfTimes(expectedRepetitions: Int) {
                val action = mockk<() -> Unit>(relaxed = true)

                timerExecutor.addRepeatingTimer(1000) {
                    action()
                }

                Thread.sleep((expectedRepetitions * 1000 + 1000).toLong())
                verify(atLeast = expectedRepetitions) {
                    action.invoke()
                    asyncExecutor.executeOnMainThread(any())
                }
            }

            @ParameterizedTest
            @ValueSource(ints = [0, 1, 3, 5])
            fun stopShouldCancelExecution(expectedRepetitions: Int) {
                val action = mockk<() -> Unit>(relaxed = true)
                val timer = timerExecutor.addRepeatingTimer(2000) {
                    action()
                }
                Thread.sleep((expectedRepetitions * 2000 + 1000).toLong())

                timer.stop()

                Thread.sleep((expectedRepetitions * 2000).toLong())
                verify(exactly = expectedRepetitions) {
                    action.invoke()
                    asyncExecutor.executeOnMainThread(any())
                }
            }
        }

    }

}