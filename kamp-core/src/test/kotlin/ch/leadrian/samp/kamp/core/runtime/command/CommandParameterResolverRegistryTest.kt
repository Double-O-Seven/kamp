package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import com.google.inject.Guice
import org.assertj.core.api.Assertions.assertThat
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
    fun shouldRegisterResolver() {
        val commandParameterResolverRegistry = CommandParameterResolverRegistry(setOf())

        commandParameterResolverRegistry.register(StringParameterResolver)

        val resolver = commandParameterResolverRegistry.getResolver(String::class.java)
        assertThat(resolver)
                .isEqualTo(StringParameterResolver)
    }

    @Test
    fun shouldBeInjectableWithMultibinding() {
        val module = object : KampModule() {

            override fun configure() {
                newCommandParameterResolverSetBinder().apply {
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