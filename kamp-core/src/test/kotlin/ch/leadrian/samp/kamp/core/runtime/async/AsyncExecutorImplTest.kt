package ch.leadrian.samp.kamp.core.runtime.async

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.util.ExecutorServiceFactory
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

internal class AsyncExecutorImplTest {

    private lateinit var asyncExecutor: AsyncExecutorImpl

    private val executorServiceFactory = mockk<ExecutorServiceFactory>()
    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val executorService = mockk<ExecutorService>()

    @BeforeEach
    fun setUp() {
        every { executorServiceFactory.createSingleThreadExecutor() } returns executorService
        every { executorService.execute(any()) } answers {
            firstArg<Runnable>().run()
        }
        asyncExecutor = AsyncExecutorImpl(callbackListenerManager, executorServiceFactory)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        asyncExecutor.initialize()

        verify { callbackListenerManager.register(asyncExecutor) }
    }

    @Nested
    inner class ShutdownTests {

        @Test
        fun givenExecutorServiceWasNotUsedItShouldDoNothing() {
            asyncExecutor.shutdown()

            verify { executorService wasNot Called }
        }

        @Test
        fun givenExecutorServiceWasUsedItShouldShutdownAndAwaitTermination() {
            every { executorService.shutdown() } just Runs
            every { executorService.awaitTermination(any(), any()) } returns true
            asyncExecutor.execute { }

            asyncExecutor.shutdown()

            verifyOrder {
                executorService.shutdown()
                executorService.awaitTermination(3, TimeUnit.MINUTES)
            }
        }

    }

    @Nested
    inner class OnProcessTickTests {

        @Test
        fun givenNoTasksToRunItShouldDoNothing() {
            val caughtThrowable = catchThrowable { asyncExecutor.onProcessTick() }

            assertThat(caughtThrowable)
                    .isNull()
        }

        @Test
        fun shouldNotExecuteOnMainThreadActionImmediately() {
            val action = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }

            asyncExecutor.executeOnMainThread(action)

            verify { action wasNot Called }
        }

        @Test
        fun shouldExecuteOnMainThreadActionOnProcessTick() {
            val action = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            asyncExecutor.executeOnMainThread(action)

            asyncExecutor.onProcessTick()

            verify(exactly = 1) { action.invoke() }
        }

        @Test
        fun shouldExecuteOnMainThreadMultipleActionsOnProcessTickInExpectedOrder() {
            val action1 = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val action2 = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val action3 = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            asyncExecutor.executeOnMainThread(action1)
            asyncExecutor.executeOnMainThread(action2)
            asyncExecutor.executeOnMainThread(action3)

            asyncExecutor.onProcessTick()

            verifyOrder {
                action1.invoke()
                action2.invoke()
                action3.invoke()
            }
        }
    }

    @Nested
    inner class ExecuteTests {

        @Test
        fun shouldExecuteWithoutOnSuccess() {
            val action = mockk<AsyncExecutor.() -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }

            asyncExecutor.execute(action = action)

            verify(exactly = 1) {
                executorService.execute(any())
                action.invoke(asyncExecutor)
            }
        }

        @Test
        fun shouldNotExecuteOnSuccessImmediately() {
            val onSuccess = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val action = mockk<AsyncExecutor.() -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }

            asyncExecutor.execute(onSuccess = onSuccess, action = action)

            verify {
                executorService.execute(any())
                action.invoke(asyncExecutor)
                onSuccess wasNot Called
            }
        }

        @Test
        fun shouldExecuteOnSuccessOnProcessTick() {
            val onSuccess = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val action = mockk<AsyncExecutor.() -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            asyncExecutor.execute(onSuccess = onSuccess, action = action)

            asyncExecutor.onProcessTick()

            verifyOrder {
                action.invoke(asyncExecutor)
                onSuccess.invoke()
            }
        }

        @Test
        fun givenExceptionItShouldNotExecuteOnFailureImmediately() {
            val onSuccess = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val onFailure = mockk<(Exception) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val action = mockk<AsyncExecutor.() -> Unit> {
                every { this@mockk.invoke(any()) } throws IllegalStateException("This is a test")
            }

            asyncExecutor.execute(onSuccess = onSuccess, onFailure = onFailure, action = action)

            verify {
                executorService.execute(any())
                action.invoke(asyncExecutor)
                onSuccess wasNot Called
                onFailure wasNot Called
            }
        }

        @Test
        fun givenExceptionItShouldExecuteOnFailureOnProcessTick() {
            val onSuccess = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val onFailure = mockk<(Exception) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val exception = IllegalStateException("This is a test")
            val action = mockk<AsyncExecutor.() -> Unit> {
                every { this@mockk.invoke(any()) } throws exception
            }
            asyncExecutor.execute(onSuccess = onSuccess, onFailure = onFailure, action = action)

            asyncExecutor.onProcessTick()

            verify { onSuccess wasNot Called }
            val slot = slot<Exception>()
            verifyOrder {
                action.invoke(asyncExecutor)
                onFailure.invoke(capture(slot))
            }
            assertThat(slot.captured)
                    .isSameAs(exception)
        }

        @Test
        fun givenExceptionItButNoOnFailureItShouldNotDoAnything() {
            val onSuccess = mockk<() -> Unit> {
                every { this@mockk.invoke() } just Runs
            }
            val exception = IllegalStateException("This is a test")
            val action = mockk<AsyncExecutor.() -> Unit> {
                every { this@mockk.invoke(any()) } throws exception
            }
            asyncExecutor.execute(onSuccess = onSuccess, action = action)

            asyncExecutor.onProcessTick()

            verify {
                action.invoke(asyncExecutor)
                onSuccess wasNot Called
            }
        }

    }

    @Nested
    inner class ExecuteWithResultTests {

        @Test
        fun shouldNotExecuteOnSuccessImmediately() {
            val onSuccess = mockk<(String) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val action = mockk<AsyncExecutor.() -> String> {
                every { this@mockk.invoke(asyncExecutor) } returns "Hi there"
            }

            asyncExecutor.executeWithResult(onSuccess = onSuccess, action = action)

            verify {
                executorService.execute(any())
                action.invoke(asyncExecutor)
                onSuccess wasNot Called
            }
        }

        @Test
        fun shouldExecuteOnSuccessOnProcessTick() {
            val onSuccess = mockk<(String) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val action = mockk<AsyncExecutor.() -> String> {
                every { this@mockk.invoke(asyncExecutor) } returns "Hi there"
            }

            asyncExecutor.executeWithResult(onSuccess = onSuccess, action = action)

            asyncExecutor.onProcessTick()

            verifyOrder {
                action.invoke(asyncExecutor)
                onSuccess.invoke("Hi there")
            }
        }

        @Test
        fun givenExceptionItShouldNotExecuteOnFailureImmediately() {
            val onSuccess = mockk<(String) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val action = mockk<AsyncExecutor.() -> String> {
                every { this@mockk.invoke(asyncExecutor) } returns "Hi there"
            }
            val onFailure = mockk<(Exception) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }

            asyncExecutor.executeWithResult(onSuccess = onSuccess, onFailure = onFailure, action = action)

            verify {
                executorService.execute(any())
                action.invoke(asyncExecutor)
                onSuccess wasNot Called
                onFailure wasNot Called
            }
        }

        @Test
        fun givenExceptionItShouldExecuteOnFailureOnProcessTick() {
            val onSuccess = mockk<(String) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val onFailure = mockk<(Exception) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val exception = IllegalStateException("This is a test")
            val action = mockk<AsyncExecutor.() -> String> {
                every { this@mockk.invoke(asyncExecutor) } throws exception
            }
            asyncExecutor.executeWithResult(onSuccess = onSuccess, onFailure = onFailure, action = action)

            asyncExecutor.onProcessTick()

            verify { onSuccess wasNot Called }
            val slot = slot<Exception>()
            verifyOrder {
                action.invoke(asyncExecutor)
                onFailure.invoke(capture(slot))
            }
            assertThat(slot.captured)
                    .isSameAs(exception)
        }

        @Test
        fun givenExceptionItButNoOnFailureItShouldNotDoAnything() {
            val onSuccess = mockk<(String) -> Unit> {
                every { this@mockk.invoke(any()) } just Runs
            }
            val exception = IllegalStateException("This is a test")
            val action = mockk<AsyncExecutor.() -> String> {
                every { this@mockk.invoke(asyncExecutor) } throws exception
            }
            asyncExecutor.executeWithResult(onSuccess = onSuccess, action = action)

            asyncExecutor.onProcessTick()

            verify {
                action.invoke(asyncExecutor)
                onSuccess wasNot Called
            }
        }

    }

}