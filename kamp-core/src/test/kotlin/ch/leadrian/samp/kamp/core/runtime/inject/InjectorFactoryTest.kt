package ch.leadrian.samp.kamp.core.runtime.inject

import com.google.inject.AbstractModule
import com.netflix.governator.lifecycle.LifecycleManager
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import javax.annotation.PostConstruct
import javax.inject.Inject

internal class InjectorFactoryTest {

    private val barService = mockk<BarService>(relaxed = true)

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            InjectorFactory.createInjector(setOf(), TestModule(barService))
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Test
    fun shouldStartExecutePostConstructMethods() {
        val injector = InjectorFactory.createInjector(setOf(), TestModule(barService))
        val lifecycleManager = injector.getInstance(LifecycleManager::class.java)

        lifecycleManager.start()

        verify { barService.bar() }
    }

    private class TestModule(private val barService: BarService) : AbstractModule() {

        override fun configure() {
            bind(BarService::class.java).toInstance(barService)
            bind(FooService::class.java).asEagerSingleton()
        }

    }

    private interface BarService {

        fun bar()

    }

    private class FooService
    @Inject
    constructor(private val barService: BarService) {

        @PostConstruct
        internal fun initialize() {
            barService.bar()
        }

    }

}