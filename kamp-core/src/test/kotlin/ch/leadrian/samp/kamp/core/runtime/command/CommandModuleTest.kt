package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultInvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultUnknownCommandHandler
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.TestModule
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import ch.leadrian.samp.kamp.core.runtime.entity.factory.EntityFactoryModule
import ch.leadrian.samp.kamp.core.runtime.text.TextModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CommandModuleTest {

    private val modules = arrayOf(
            TestModule(),
            TextModule(),
            CommandModule(),
            CallbackModule(),
            EntityFactoryModule()
    )

    @Test
    fun shouldCreateInjector() {
        val caughtThrowable = catchThrowable {
            Guice.createInjector(*modules)
        }

        assertThat(caughtThrowable)
                .isNull()
    }

    @Nested
    inner class InjectionTests {

        private lateinit var injector: Injector

        @BeforeEach
        fun setUp() {
            injector = Guice.createInjector(*modules)
        }

        @Test
        fun shouldInjectCommandProcessorAsSingleton() {
            val commandProcessor = injector.getInstance<CommandProcessor>()

            assertThat(commandProcessor)
                    .isNotNull
                    .isSameAs(injector.getInstance<CommandProcessor>())
        }

        @Test
        fun shouldInjectCommandParser() {
            val commandParser = injector.getInstance<CommandParser>()

            assertThat(commandParser)
                    .isNotNull
        }

        @Test
        fun shouldInjectCommandRegistryAsSingleton() {
            val commandRegistry = injector.getInstance<CommandRegistry>()

            assertThat(commandRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<CommandRegistry>())
        }

        @Test
        fun shouldInjectCommandDefinitionLoader() {
            val commandDefinitionLoader = injector.getInstance<CommandDefinitionLoader>()

            assertThat(commandDefinitionLoader)
                    .isNotNull
        }

        @Test
        fun shouldInjectCommandParameterResolverRegistryAsSingleton() {
            val commandParameterResolverRegistry = injector.getInstance<CommandParameterResolverRegistry>()

            assertThat(commandParameterResolverRegistry)
                    .isNotNull
                    .isSameAs(injector.getInstance<CommandParameterResolverRegistry>())
        }

        @Test
        fun shouldInjectCommandParameterResolverFactory() {
            val commandParameterResolverFactory = injector.getInstance<CommandParameterResolverFactory>()

            assertThat(commandParameterResolverFactory)
                    .isNotNull
        }

        @Test
        fun shouldInjectCommandParametersResolver() {
            val commandParametersResolver = injector.getInstance<CommandParametersResolver>()

            assertThat(commandParametersResolver)
                    .isNotNull
        }

        @Test
        fun shouldInjectCommandExecutor() {
            val commandExecutor = injector.getInstance<CommandMethodInvoker>()

            assertThat(commandExecutor)
                    .isNotNull
        }

        @Test
        fun shouldInjectDefaultCommandAccessDeniedHandler() {
            val defaultCommandAccessDeniedHandler = injector.getInstance<DefaultCommandAccessDeniedHandler>()

            assertThat(defaultCommandAccessDeniedHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectDefaultCommandErrorHandler() {
            val defaultCommandErrorHandler = injector.getInstance<DefaultCommandErrorHandler>()

            assertThat(defaultCommandErrorHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectDefaultInvalidCommandParameterValueHandler() {
            val defaultInvalidCommandParameterValueHandler = injector.getInstance<DefaultInvalidCommandParameterValueHandler>()

            assertThat(defaultInvalidCommandParameterValueHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectDefaultUnknownCommandHandler() {
            val defaultUnknownCommandHandler = injector.getInstance<DefaultUnknownCommandHandler>()

            assertThat(defaultUnknownCommandHandler)
                    .isNotNull
        }

        @Test
        fun shouldInjectSetOfCommandParameterResolvers() {
            val fooService = injector.getInstance<FooService>()

            val commandParameterResolverClasses = fooService.commandParameterResolvers.map { it::class.java }
            assertThat(commandParameterResolverClasses)
                    .containsExactlyInAnyOrder(
                            ActorCommandParameterResolver::class.java,
                            DoubleCommandParameterResolver::class.java,
                            FloatCommandParameterResolver::class.java,
                            GangZoneCommandParameterResolver::class.java,
                            IntCommandParameterResolver::class.java,
                            LongCommandParameterResolver::class.java,
                            MapObjectCommandParameterResolver::class.java,
                            MenuCommandParameterResolver::class.java,
                            PickupCommandParameterResolver::class.java,
                            PlayerCommandParameterResolver::class.java,
                            PrimitiveDoubleCommandParameterResolver::class.java,
                            PrimitiveFloatCommandParameterResolver::class.java,
                            PrimitiveIntCommandParameterResolver::class.java,
                            PrimitiveLongCommandParameterResolver::class.java,
                            StringCommandParameterResolver::class.java,
                            TextDrawCommandParameterResolver::class.java,
                            TextLabelCommandParameterResolver::class.java,
                            VehicleCommandParameterResolver::class.java,
                            VehicleModelCommandParameterResolver::class.java,
                            WeaponModelCommandParameterResolver::class.java
                    )
        }

    }

    private class FooService
    @Inject
    constructor(val commandParameterResolvers: Set<@JvmSuppressWildcards CommandParameterResolver<*>>)

}