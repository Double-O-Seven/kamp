package ch.leadrian.samp.kamp.core.runtime.async

import ch.leadrian.samp.kamp.core.api.async.AsyncExecutor
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import com.google.inject.Guice
import com.google.inject.Injector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class AsyncModuleTest {

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(AsyncModule(), CallbackModule())
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(AsyncModule(), CallbackModule())
        }

        @Test
        fun shouldInjectAsyncExecutor() {
            val asyncExecutor = injector.getInstance<AsyncExecutor>()

            assertThat(asyncExecutor)
                    .isInstanceOf(AsyncExecutorImpl::class.java)
        }

        @Test
        fun shouldInjectExecutorServiceFactory() {
            val executorServiceFactory = injector.getInstance<ExecutorServiceFactory>()

            assertThat(executorServiceFactory)
                    .isNotNull
        }
    }
}