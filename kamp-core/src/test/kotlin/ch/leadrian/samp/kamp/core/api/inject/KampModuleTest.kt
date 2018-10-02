package ch.leadrian.samp.kamp.core.api.inject

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.inject.Named

internal class KampModuleTest {

    private lateinit var injector: Injector

    @BeforeEach
    fun setUp() {
        injector = Guice.createInjector(FooModule(), BarModule(), TestModule())
    }

    @Test
    fun shouldInjectCommandParameterResolvers() {
        val fooService = injector.getInstance(FooService::class.java)

        assertThat(fooService.resolvers)
                .containsExactlyInAnyOrder(StringParameterResolver, IntParameterResolver)
    }

    @Test
    fun shouldInjectResourceBundlePackages() {
        val barService = injector.getInstance(BarService::class.java)

        assertThat(barService.resourceBundlePackages)
                .containsExactlyInAnyOrder("ch.leadrian.samp.kamp.foo", "ch.leadrian.samp.kamp.bar")
    }

    @Test
    fun shouldInjectCommands() {
        val bazService = injector.getInstance(BazService::class.java)

        assertThat(bazService.commands)
                .containsExactlyInAnyOrder(FooCommands, BarCommands)
    }

    @Test
    fun shouldInjectCallbackListenerRegistries() {
        val quxService = injector.getInstance(QuxService::class.java)

        assertThat(quxService.callbackListenerRegistries)
                .containsExactlyInAnyOrder(FooCallbackListenerRegistry, BarCallbackListenerRegistry)
    }

    private class FooService
    @Inject
    constructor(val resolvers: Set<@JvmSuppressWildcards CommandParameterResolver<*>>)

    private class BarService
    @Inject
    constructor(
            @Named(TextProvider.RESOURCE_BUNDLE_PACKAGES_NAME)
            val resourceBundlePackages: Set<@JvmSuppressWildcards String>
    )

    private class BazService
    @Inject
    constructor(val commands: Set<@JvmSuppressWildcards Commands>)

    private class QuxService
    @Inject
    constructor(val callbackListenerRegistries: Set<@JvmSuppressWildcards CallbackListenerRegistry<*>>)

    private class FooModule : KampModule() {

        override fun configure() {
            newCommandParameterResolverSetBinder().apply {
                addBinding().toInstance(StringParameterResolver)
            }
            newTextProviderResourceBundlePackagesSetBinder().apply {
                addBinding().toInstance("ch.leadrian.samp.kamp.foo")
            }
            newCommandsSetBinder().apply {
                addBinding().toInstance(FooCommands)
            }
            newCallbackListenerRegistry().apply {
                addBinding().toInstance(FooCallbackListenerRegistry)
            }
        }

    }

    private class BarModule : KampModule() {

        override fun configure() {
            newCommandParameterResolverSetBinder().apply {
                addBinding().toInstance(IntParameterResolver)
            }
            newTextProviderResourceBundlePackagesSetBinder().apply {
                addBinding().toInstance("ch.leadrian.samp.kamp.bar")
            }
            newCommandsSetBinder().apply {
                addBinding().toInstance(BarCommands)
            }
            newCallbackListenerRegistry().apply {
                addBinding().toInstance(BarCallbackListenerRegistry)
            }
        }

    }

    private object StringParameterResolver : CommandParameterResolver<String> {

        override val parameterType: Class<String>
            get() = String::class.java

        override fun resolve(value: String): String = throw UnsupportedOperationException("test")

    }

    private object IntParameterResolver : CommandParameterResolver<Int> {

        override val parameterType: Class<Int>
            get() = Int::class.javaObjectType

        override fun resolve(value: String): Int = throw UnsupportedOperationException("test")

    }

    private object FooCallbackListenerRegistry : CallbackListenerRegistry<FooCallback>(FooCallback::class)

    private object BarCallbackListenerRegistry : CallbackListenerRegistry<BarCallback>(BarCallback::class)

    private interface FooCallback

    private interface BarCallback

    private object FooCommands : Commands()

    private object BarCommands : Commands()

    private class TestModule : AbstractModule() {

        override fun configure() {
            bind(SAMPNativeFunctionExecutor::class.java).toInstance(mockk())
        }

    }

}