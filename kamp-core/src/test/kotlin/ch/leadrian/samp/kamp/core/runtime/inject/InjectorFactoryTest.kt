package ch.leadrian.samp.kamp.core.runtime.inject

import ch.leadrian.samp.kamp.core.api.util.getInstance
import com.google.inject.AbstractModule
import com.netflix.governator.annotations.Configuration
import com.netflix.governator.configuration.PropertiesConfigurationProvider
import com.netflix.governator.guice.BootstrapBinder
import com.netflix.governator.guice.BootstrapModule
import com.netflix.governator.lifecycle.LifecycleManager
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import java.util.Properties
import javax.annotation.PostConstruct
import javax.inject.Inject

internal class InjectorFactoryTest {

    private val barService = mockk<BarService>(relaxed = true)

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            InjectorFactory.createInjector(setOf(), TestBootstrapModule, TestModule(barService))
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Test
    fun shouldStartExecutePostConstructMethods() {
        val injector = InjectorFactory.createInjector(setOf(), TestBootstrapModule, TestModule(barService))
        val lifecycleManager = injector.getInstance(LifecycleManager::class.java)

        lifecycleManager.start()

        verify { barService.bar() }
    }

    @Test
    fun shouldInjectConfigurations() {
        val injector = InjectorFactory.createInjector(setOf(), TestBootstrapModule, TestModule(barService))

        val quxService = injector.getInstance<QuxService>()

        assertThat(quxService.foo)
                .isEqualTo("Foo!!!")
        assertThat(quxService.bar)
                .isEqualTo(1337)
    }

    private class TestModule(private val barService: BarService) : AbstractModule() {

        override fun configure() {
            bind(BarService::class.java).toInstance(barService)
            bind(FooService::class.java).asEagerSingleton()
        }

    }

    private object TestBootstrapModule : BootstrapModule {

        override fun configure(binder: BootstrapBinder) {
            val properties = Properties().apply {
                setProperty("test.foo", "Foo!!!")
                setProperty("test.bar", "1337")
            }
            binder.bindConfigurationProvider().toInstance(PropertiesConfigurationProvider(properties))
        }

    }

    private interface BarService {

        fun bar()

    }

    private class FooService
    @Inject
    constructor(private val barService: BarService) {

        @Suppress("unused")
        @PostConstruct
        internal fun initialize() {
            barService.bar()
        }

    }

    private class QuxService
    @Inject
    constructor() {

        @Configuration("test.foo")
        lateinit var foo: String

        @Configuration("test.bar")
        var bar: Int = 0
    }

}