package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.TypeLiteral
import com.google.inject.multibindings.Multibinder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test


internal class CommandParameterResolverRegistryTest {

    @Test
    fun shouldReturnCommandParameterResolver() {
        val commandParameterResolverRegistry = CommandParameterResolverRegistry(setOf(StringParameterResolver))

        val resolver = commandParameterResolverRegistry.getResolver(String::class.java)

        assertThat(resolver)
                .isEqualTo(StringParameterResolver)
    }

    @Test
    fun givenNoResolverForParameterTypeItShouldException() {
        val commandParameterResolverRegistry = CommandParameterResolverRegistry(setOf(StringParameterResolver))

        val caughtThrowable = catchThrowable { commandParameterResolverRegistry.getResolver(Int::class.java) }

        assertThat(caughtThrowable)
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun shouldBeInjectableWithMultibinding() {
        val module = object : AbstractModule() {

            override fun configure() {
                Multibinder.newSetBinder(binder(), object : TypeLiteral<CommandParameterResolver<*>>() {}).apply {
                    addBinding().toInstance(StringParameterResolver)
                }
            }

        }
        val injector = Guice.createInjector(module)
        val commandParameterResolverRegistry = injector.getInstance(CommandParameterResolverRegistry::class.java)

        val resolver = commandParameterResolverRegistry.getResolver(String::class.java)

        assertThat(resolver)
                .isEqualTo(StringParameterResolver)
    }

    private object StringParameterResolver : CommandParameterResolver<String> {

        override val parameterType: Class<String>
            get() = String::class.java

        override fun resolve(value: String): String = throw UnsupportedOperationException("test")

    }

}