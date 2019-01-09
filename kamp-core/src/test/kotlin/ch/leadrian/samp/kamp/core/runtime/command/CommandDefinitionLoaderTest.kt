package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.CommandAccessCheckerGroup
import ch.leadrian.samp.kamp.core.api.command.CommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandDescription
import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.CommandParameterDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.command.Commands
import ch.leadrian.samp.kamp.core.api.command.InvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.annotation.AccessCheck
import ch.leadrian.samp.kamp.core.api.command.annotation.AccessChecks
import ch.leadrian.samp.kamp.core.api.command.annotation.Command
import ch.leadrian.samp.kamp.core.api.command.annotation.CommandGroup
import ch.leadrian.samp.kamp.core.api.command.annotation.Description
import ch.leadrian.samp.kamp.core.api.command.annotation.ErrorHandler
import ch.leadrian.samp.kamp.core.api.command.annotation.InvalidParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.annotation.Parameter
import ch.leadrian.samp.kamp.core.api.command.annotation.Unlisted
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

@Suppress("UNUSED")
class CommandDefinitionLoaderTest {

    private lateinit var commandDefinitionLoader: CommandDefinitionLoader

    private lateinit var injector: Injector
    private val textProvider = mockk<TextProvider>()
    private val commandParameterResolverFactory = mockk<CommandParameterResolverFactory>()

    @BeforeEach
    fun setUp() {
        every {
            commandParameterResolverFactory
                    .getResolver(Int::class.javaPrimitiveType!!)
        } returns PrimitiveIntParameterResolver
        every { commandParameterResolverFactory.getResolver(Int::class.javaObjectType) } returns IntParameterResolver
        every { commandParameterResolverFactory.getResolver(String::class.java) } returns StringParameterResolver
        injector = Guice.createInjector(TestModule())
        commandDefinitionLoader = CommandDefinitionLoader(
                injector = injector,
                textProvider = textProvider,
                commandParameterResolverFactory = commandParameterResolverFactory
        )
    }

    @Test
    fun shouldLoadCommandParameterResolvers() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsForCommandParameterResolverTests)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "say",
                                commandsInstance = CommandsForCommandParameterResolverTests,
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "say",
                                        Player::class.java,
                                        String::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = String::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "setname",
                                commandsInstance = CommandsForCommandParameterResolverTests,
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "setName",
                                        Player::class.java,
                                        Int::class.javaPrimitiveType,
                                        String::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = Int::class.javaPrimitiveType!!,
                                                resolver = PrimitiveIntParameterResolver,
                                                textProvider = textProvider
                                        ),
                                        CommandParameterDefinition(
                                                type = String::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "banall",
                                commandsInstance = CommandsForCommandParameterResolverTests,
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "banAll",
                                        Player::class.java,
                                        String::class.java,
                                        List::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = String::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        ),
                                        CommandParameterDefinition(
                                                type = List::class.javaObjectType,
                                                resolver = IntParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "kill",
                                commandsInstance = CommandsForCommandParameterResolverTests,
                                method = CommandsForCommandParameterResolverTests::class.java.getMethod(
                                        "kill",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsForCommandParameterResolverTests : Commands() {

        @Command
        fun say(player: Player, message: String) {
        }

        @Command
        fun setName(player: Player, playerId: Int, name: String) {
        }

        @Command
        fun banAll(player: Player, reason: String, playerIds: List<Int>) {
        }

        @Command
        fun kill(player: Player) {
        }

    }

    @Test
    fun shouldSetCommandDefinitionsOnCommandsInstance() {
        val commandsInstance = BoringCommands()

        val commandDefinitions = commandDefinitionLoader.load(commandsInstance)

        assertThat(commandsInstance.definitions)
                .isSameAs(commandDefinitions)
    }

    @Suppress("UNUSED_PARAMETER")
    private class BoringCommands : Commands() {

        @Command
        fun foo(player: Player) {
        }

        @Command
        fun bar(player: Player) {
        }

        @Command
        fun baz(player: Player) {
        }
    }

    @Test
    fun shouldLoadDescriptions() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithDescriptions)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithDescriptions,
                                description = CommandDescription(
                                        text = "Foo command",
                                        textKey = TextKey("command.foo.description"),
                                        textProvider = textProvider
                                ),
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "bar",
                                commandsInstance = CommandsWithDescriptions,
                                description = CommandDescription(
                                        textKey = TextKey("command.bar.description"),
                                        textProvider = textProvider
                                ),
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "bar",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "baz",
                                commandsInstance = CommandsWithDescriptions,
                                description = CommandDescription(
                                        text = "Baz command",
                                        textProvider = textProvider
                                ),
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "baz",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "lol",
                                commandsInstance = CommandsWithDescriptions,
                                method = CommandsWithDescriptions::class.java.getMethod(
                                        "lol",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithDescriptions : Commands() {

        @Command
        @Description(text = "Foo command", textKey = "command.foo.description")
        fun foo(player: Player) {
        }

        @Command
        @Description(textKey = "command.bar.description")
        fun bar(player: Player) {
        }

        @Command
        @Description(text = "Baz command")
        fun baz(player: Player) {
        }

        @Command
        @Description
        fun lol(player: Player) {
        }

    }

    @Test
    fun shouldLoadAliases() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithAliases)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithAliases,
                                aliases = setOf("f", "bar", "baz"),
                                method = CommandsWithAliases::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithAliases : Commands() {

        @Command(aliases = ["f", "bar", "baz"])
        fun foo(player: Player) {
        }
    }

    @Test
    fun shouldLoadGreediness() {
        val commandDefinitions = commandDefinitionLoader.load(GreedyCommands)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = GreedyCommands,
                                isGreedy = true,
                                method = GreedyCommands::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "bar",
                                commandsInstance = GreedyCommands,
                                isGreedy = false,
                                method = GreedyCommands::class.java.getMethod(
                                        "bar",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "baz",
                                commandsInstance = GreedyCommands,
                                isGreedy = true,
                                method = GreedyCommands::class.java.getMethod(
                                        "baz",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object GreedyCommands : Commands() {

        @Command(isGreedy = true)
        fun foo(player: Player) {
        }

        @Command(isGreedy = false)
        fun bar(player: Player) {
        }

        @Command
        fun baz(player: Player) {
        }
    }

    @Test
    fun shouldLoadErrorHandler() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithErrorHandler)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithErrorHandler,
                                method = CommandsWithErrorHandler::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf(),
                                errorHandler = FooCommandErrorHandler
                        ),
                        CommandDefinition(
                                name = "bar",
                                commandsInstance = CommandsWithErrorHandler,
                                method = CommandsWithErrorHandler::class.java.getMethod(
                                        "bar",
                                        Player::class.java
                                ),
                                parameters = listOf(),
                                errorHandler = BarCommandErrorHandler
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    @ErrorHandler(FooCommandErrorHandler::class)
    private object CommandsWithErrorHandler : Commands() {

        @Command
        fun foo(player: Player) {
        }

        @ErrorHandler(BarCommandErrorHandler::class)
        @Command
        fun bar(player: Player) {
        }
    }

    @Test
    fun shouldLoadAccessCheckers() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithAccessCheckers)


        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithAccessCheckers,
                                method = CommandsWithAccessCheckers::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf(),
                                accessCheckers = listOf(
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(FooCommandAccessChecker),
                                                accessDeniedHandlers = listOf(FooCommandAccessDeniedHandler),
                                                errorMessage = "Foo error",
                                                errorMessageTextKey = TextKey("foo.error"),
                                                textProvider = textProvider
                                        ),
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(BarCommandAccessChecker),
                                                accessDeniedHandlers = listOf(BarCommandAccessDeniedHandler),
                                                errorMessageTextKey = TextKey("bar.error"),
                                                textProvider = textProvider
                                        ),
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(BatCommandAccessChecker),
                                                errorMessage = "Bat error",
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "baz",
                                commandsInstance = CommandsWithAccessCheckers,
                                method = CommandsWithAccessCheckers::class.java.getMethod(
                                        "baz",
                                        Player::class.java
                                ),
                                parameters = listOf(),
                                accessCheckers = listOf(
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(
                                                        BazCommandAccessChecker,
                                                        FoobarCommandAccessChecker
                                                ),
                                                accessDeniedHandlers = listOf(BazCommandAccessDeniedHandler),
                                                errorMessage = "Baz error",
                                                errorMessageTextKey = TextKey("baz.error"),
                                                textProvider = textProvider
                                        ),
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(QuxCommandAccessChecker),
                                                accessDeniedHandlers = listOf(QuxCommandAccessDeniedHandler),
                                                textProvider = textProvider
                                        ),
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(FooCommandAccessChecker),
                                                accessDeniedHandlers = listOf(FooCommandAccessDeniedHandler),
                                                errorMessage = "Foo error",
                                                errorMessageTextKey = TextKey("foo.error"),
                                                textProvider = textProvider
                                        ),
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(BarCommandAccessChecker),
                                                accessDeniedHandlers = listOf(BarCommandAccessDeniedHandler),
                                                errorMessageTextKey = TextKey("bar.error"),
                                                textProvider = textProvider
                                        ),
                                        CommandAccessCheckerGroup(
                                                accessCheckers = listOf(BatCommandAccessChecker),
                                                errorMessage = "Bat error",
                                                textProvider = textProvider
                                        )
                                )
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    @AccessCheck(
            accessCheckers = [FooCommandAccessChecker::class],
            accessDeniedHandlers = [FooCommandAccessDeniedHandler::class],
            errorMessage = "Foo error",
            errorMessageTextKey = "foo.error"
    )
    @AccessChecks(
            [
                AccessCheck(
                        accessCheckers = [BarCommandAccessChecker::class],
                        errorMessageTextKey = "bar.error",
                        accessDeniedHandlers = [BarCommandAccessDeniedHandler::class]
                ),
                AccessCheck(
                        accessCheckers = [BatCommandAccessChecker::class],
                        errorMessage = "Bat error"
                )
            ]
    )
    private object CommandsWithAccessCheckers : Commands() {

        @Command
        fun foo(player: Player) {
        }

        @Command
        @AccessCheck(
                accessCheckers = [BazCommandAccessChecker::class, FoobarCommandAccessChecker::class],
                accessDeniedHandlers = [BazCommandAccessDeniedHandler::class],
                errorMessage = "Baz error",
                errorMessageTextKey = "baz.error"
        )
        @AccessChecks(
                [
                    AccessCheck(
                            accessCheckers = [QuxCommandAccessChecker::class],
                            accessDeniedHandlers = [QuxCommandAccessDeniedHandler::class]
                    )
                ]
        )
        fun baz(player: Player) {
        }

    }

    @Test
    fun shouldLoadWhetherCommandsAreListed() {
        val commandDefinitions = commandDefinitionLoader.load(ListedAndUnlistedCommands)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = ListedAndUnlistedCommands,
                                method = ListedAndUnlistedCommands::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf(),
                                isListed = false
                        ),
                        CommandDefinition(
                                name = "bar",
                                commandsInstance = ListedAndUnlistedCommands,
                                method = ListedAndUnlistedCommands::class.java.getMethod(
                                        "bar",
                                        Player::class.java
                                ),
                                parameters = listOf(),
                                isListed = true
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object ListedAndUnlistedCommands : Commands() {

        @Unlisted
        @Command
        fun foo(player: Player) {
        }

        @Command
        fun bar(player: Player) {
        }
    }

    @Test
    fun shouldLoadParameters() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithParameters)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithParameters,
                                method = CommandsWithParameters::class.java.getMethod(
                                        "foo",
                                        Player::class.java,
                                        Int::class.javaPrimitiveType!!,
                                        String::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = Int::class.javaPrimitiveType!!,
                                                name = "foobar",
                                                nameTextKey = TextKey("foo.bar"),
                                                description = "Foobar param",
                                                descriptionTextKey = TextKey("foo.bar.description"),
                                                textProvider = textProvider,
                                                resolver = PrimitiveIntParameterResolver,
                                                invalidCommandParameterValueHandler = BarInvalidParameterValueHandler
                                        ),
                                        CommandParameterDefinition(
                                                type = String::class.java,
                                                textProvider = textProvider,
                                                resolver = StringParameterResolver,
                                                invalidCommandParameterValueHandler = FooInvalidParameterValueHandler
                                        )
                                )
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithParameters : Commands() {

        @Command
        @InvalidParameterValueHandler(FooInvalidParameterValueHandler::class)
        fun foo(
                player: Player,
                @Parameter(
                        name = "foobar",
                        nameTextKey = "foo.bar",
                        description = "Foobar param",
                        descriptionTextKey = "foo.bar.description"
                )
                @InvalidParameterValueHandler(BarInvalidParameterValueHandler::class)
                bar: Int,
                baz: String
        ) {
        }

    }

    @Test
    fun shouldOnlyLoadCommandsWithCommandAnnotation() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithMissingAnnotations)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithMissingAnnotations,
                                method = CommandsWithMissingAnnotations::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithMissingAnnotations : Commands() {

        @Command
        fun foo(player: Player) {
        }

        fun bar(player: Player) {
        }
    }

    @Test
    fun shouldLoadCommandGroupName() {
        val commandsInstance = CommandsWithGroupName()
        val commandDefinitions = commandDefinitionLoader.load(commandsInstance)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = commandsInstance,
                                groupName = "test",
                                method = CommandsWithGroupName::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
        assertThat(commandsInstance.groupName)
                .isEqualTo("test")
    }

    @Suppress("UNUSED_PARAMETER")
    @CommandGroup("test")
    private class CommandsWithGroupName : Commands() {

        @Command
        fun foo(player: Player) {
        }

    }

    @ParameterizedTest
    @ArgumentsSource(CommandsWithInvalidMethodsArgumentsProvider::class)
    fun givenCommandWithNonPublicOrStaticMethodItShouldThrowException(commands: Commands) {
        val caughtThrowable = catchThrowable { commandDefinitionLoader.load(commands) }

        assertThat(caughtThrowable)
                .isInstanceOf(CommandDefinitionLoaderException::class.java)
    }

    private class CommandsWithInvalidMethodsArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(CommandsWithPrivateMethod),
                        Arguments.of(CommandsWithStaticMethod()),
                        Arguments.of(CommandsWithProtectedMethod())
                )

    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithPrivateMethod : Commands() {

        @Command
        private fun foo(player: Player) {
        }

    }

    @Suppress("UNUSED_PARAMETER")
    private class CommandsWithStaticMethod : Commands() {

        companion object {

            @Command
            @JvmStatic
            fun foo(player: Player) {
            }
        }

    }

    @Suppress("UNUSED_PARAMETER")
    private open class CommandsWithProtectedMethod : Commands() {

        @Command
        protected fun foo(player: Player) {
        }

    }

    @ParameterizedTest
    @ArgumentsSource(InvalidFirstParameterArgumentsProvider::class)
    fun givenCommandsWithoutPlayerAsFirstMethodParameterItShouldThrowAnException(commands: Commands) {
        val caughtThrowable = catchThrowable { commandDefinitionLoader.load(commands) }

        assertThat(caughtThrowable)
                .isInstanceOf(CommandDefinitionLoaderException::class.java)
    }

    private class InvalidFirstParameterArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(CommandsWithoutPlayerAsFirstParameter),
                        Arguments.of(CommandsWithoutMethodParameters)
                )

    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithoutPlayerAsFirstParameter : Commands() {

        @Command
        fun foo(bar: Int, player: Player) {
        }

    }

    private object CommandsWithoutMethodParameters : Commands() {

        @Command
        fun foo() {
        }

    }

    @Test
    fun shouldLoadMethodsWithValidReturnTypes() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithValidMethodReturnTypes)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithValidMethodReturnTypes,
                                method = CommandsWithValidMethodReturnTypes::class.java.getMethod(
                                        "foo",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "bar",
                                commandsInstance = CommandsWithValidMethodReturnTypes,
                                method = CommandsWithValidMethodReturnTypes::class.java.getMethod(
                                        "bar",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "baz",
                                commandsInstance = CommandsWithValidMethodReturnTypes,
                                method = CommandsWithValidMethodReturnTypes::class.java.getMethod(
                                        "baz",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "bat",
                                commandsInstance = CommandsWithValidMethodReturnTypes,
                                method = CommandsWithValidMethodReturnTypes::class.java.getMethod(
                                        "bat",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "qux",
                                commandsInstance = CommandsWithValidMethodReturnTypes,
                                method = CommandsWithValidMethodReturnTypes::class.java.getMethod(
                                        "qux",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        ),
                        CommandDefinition(
                                name = "foobar",
                                commandsInstance = CommandsWithValidMethodReturnTypes,
                                method = CommandsWithValidMethodReturnTypes::class.java.getMethod(
                                        "foobar",
                                        Player::class.java
                                ),
                                parameters = listOf()
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithValidMethodReturnTypes : Commands() {

        @Command
        fun foo(player: Player) {
        }

        @Command
        fun bar(player: Player): Boolean {
            return true
        }

        @Command
        fun baz(player: Player): Boolean? {
            return null
        }

        @Command
        fun bat(player: Player): OnPlayerCommandTextListener.Result {
            return OnPlayerCommandTextListener.Result.Processed
        }

        @Command
        fun qux(player: Player): OnPlayerCommandTextListener.Result.Processed {
            return OnPlayerCommandTextListener.Result.Processed
        }

        @Command
        fun foobar(player: Player): OnPlayerCommandTextListener.Result.UnknownCommand {
            return OnPlayerCommandTextListener.Result.UnknownCommand
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidReturnTypeArgumentsProvider::class)
    fun givenCommandWithInvalidReturnTypeItShouldThrowException(commands: Commands) {
        val caughtThrowable = catchThrowable { commandDefinitionLoader.load(commands) }

        assertThat(caughtThrowable)
                .isInstanceOf(CommandDefinitionLoaderException::class.java)
    }

    private class InvalidReturnTypeArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(CommandsWithIntReturnType),
                        Arguments.of(CommandsWithFloatReturnType),
                        Arguments.of(CommandsWithAnyReturnType),
                        Arguments.of(CommandsWithStringReturnType)
                )

    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithIntReturnType : Commands() {

        @Command
        fun foo(player: Player): Int {
            return 1337
        }

    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithFloatReturnType : Commands() {

        @Command
        fun foo(player: Player): Float {
            return 1337f
        }

    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithAnyReturnType : Commands() {

        @Command
        fun foo(player: Player): Any {
            return Any()
        }

    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithStringReturnType : Commands() {

        @Command
        fun foo(player: Player): String {
            return "Hi there"
        }

    }

    @Test
    fun shouldLoadMethodsWithValidCollectionTypes() {
        val commandDefinitions = commandDefinitionLoader.load(CommandsWithValidCollectionTypes)

        assertThat(commandDefinitions)
                .containsExactlyInAnyOrder(
                        CommandDefinition(
                                name = "foo",
                                commandsInstance = CommandsWithValidCollectionTypes,
                                method = CommandsWithValidCollectionTypes::class.java.getMethod(
                                        "foo",
                                        Player::class.java,
                                        Iterable::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = Iterable::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "bar",
                                commandsInstance = CommandsWithValidCollectionTypes,
                                method = CommandsWithValidCollectionTypes::class.java.getMethod(
                                        "bar",
                                        Player::class.java,
                                        Collection::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = Collection::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "baz",
                                commandsInstance = CommandsWithValidCollectionTypes,
                                method = CommandsWithValidCollectionTypes::class.java.getMethod(
                                        "baz",
                                        Player::class.java,
                                        List::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = List::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        ),
                        CommandDefinition(
                                name = "bat",
                                commandsInstance = CommandsWithValidCollectionTypes,
                                method = CommandsWithValidCollectionTypes::class.java.getMethod(
                                        "bat",
                                        Player::class.java,
                                        Set::class.java
                                ),
                                parameters = listOf(
                                        CommandParameterDefinition(
                                                type = Set::class.java,
                                                resolver = StringParameterResolver,
                                                textProvider = textProvider
                                        )
                                )
                        )
                )
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithValidCollectionTypes : Commands() {

        @Command
        fun foo(player: Player, params: Iterable<String>) {
        }

        @Command
        fun bar(player: Player, params: Collection<String>) {
        }

        @Command
        fun baz(player: Player, params: List<String>) {
        }

        @Command
        fun bat(player: Player, params: Set<String>) {
        }

    }

    @Test
    fun givenCollectionTypeParameterIsNotTheLastParameterItShouldThrowException() {
        val caughtThrowable = catchThrowable {
            commandDefinitionLoader.load(CommandsWithCollectionParameterNotInLastPlace)
        }

        assertThat(caughtThrowable)
                .isInstanceOf(CommandDefinitionLoaderException::class.java)
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithCollectionParameterNotInLastPlace : Commands() {

        @Command
        fun foo(player: Player, bar: Iterable<String>, baz: Int) {
        }
    }

    @Test
    fun givenInvalidCollectionTypeArgumentItShouldThrowException() {
        val caughtThrowable = catchThrowable {
            commandDefinitionLoader.load(CommandsWithInvalidCollectionTypeArgument)
        }

        assertThat(caughtThrowable)
                .isInstanceOf(CommandDefinitionLoaderException::class.java)
    }

    @Suppress("UNUSED_PARAMETER")
    private object CommandsWithInvalidCollectionTypeArgument : Commands() {

        @Command
        fun <T> foo(player: Player, bar: Iterable<T>) {
        }
    }

    private class TestModule : AbstractModule() {

        override fun configure() {
            bind(FooInvalidParameterValueHandler::class.java).toInstance(FooInvalidParameterValueHandler)
            bind(BarInvalidParameterValueHandler::class.java).toInstance(BarInvalidParameterValueHandler)
            bind(FooCommandAccessChecker::class.java).toInstance(FooCommandAccessChecker)
            bind(BarCommandAccessChecker::class.java).toInstance(BarCommandAccessChecker)
            bind(BazCommandAccessChecker::class.java).toInstance(BazCommandAccessChecker)
            bind(BatCommandAccessChecker::class.java).toInstance(BatCommandAccessChecker)
            bind(QuxCommandAccessChecker::class.java).toInstance(QuxCommandAccessChecker)
            bind(FoobarCommandAccessChecker::class.java).toInstance(FoobarCommandAccessChecker)
            bind(FooCommandAccessDeniedHandler::class.java).toInstance(FooCommandAccessDeniedHandler)
            bind(BarCommandAccessDeniedHandler::class.java).toInstance(BarCommandAccessDeniedHandler)
            bind(BazCommandAccessDeniedHandler::class.java).toInstance(BazCommandAccessDeniedHandler)
            bind(QuxCommandAccessDeniedHandler::class.java).toInstance(QuxCommandAccessDeniedHandler)
            bind(FooCommandErrorHandler::class.java).toInstance(FooCommandErrorHandler)
            bind(BarCommandErrorHandler::class.java).toInstance(BarCommandErrorHandler)
        }

    }

    private object FooInvalidParameterValueHandler : InvalidCommandParameterValueHandler {

        override fun handle(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>,
                parameterIndex: Int?
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    private object BarInvalidParameterValueHandler : InvalidCommandParameterValueHandler {

        override fun handle(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>,
                parameterIndex: Int?
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

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

    private object PrimitiveIntParameterResolver : CommandParameterResolver<Int> {

        override val parameterType: Class<Int>
            get() = Int::class.javaPrimitiveType!!

        override fun resolve(value: String): Int = throw UnsupportedOperationException("test")

    }

    private object FooCommandAccessChecker : CommandAccessChecker {

        override fun hasAccess(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): Boolean = true

    }

    private object BarCommandAccessChecker : CommandAccessChecker {

        override fun hasAccess(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): Boolean = true

    }

    private object BazCommandAccessChecker : CommandAccessChecker {

        override fun hasAccess(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): Boolean = true

    }

    private object BatCommandAccessChecker : CommandAccessChecker {

        override fun hasAccess(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): Boolean = true

    }

    private object QuxCommandAccessChecker : CommandAccessChecker {

        override fun hasAccess(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): Boolean = true

    }

    private object FoobarCommandAccessChecker : CommandAccessChecker {

        override fun hasAccess(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): Boolean = true

    }

    private object FooCommandAccessDeniedHandler : CommandAccessDeniedHandler {

        override fun handle(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    private object BarCommandAccessDeniedHandler : CommandAccessDeniedHandler {

        override fun handle(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    private object BazCommandAccessDeniedHandler : CommandAccessDeniedHandler {

        override fun handle(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    private object QuxCommandAccessDeniedHandler : CommandAccessDeniedHandler {

        override fun handle(
                player: Player,
                commandDefinition: CommandDefinition,
                parameters: List<String>
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    private object FooCommandErrorHandler : CommandErrorHandler {

        override fun handle(
                player: Player,
                commandLine: String,
                exception: Exception?
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

    private object BarCommandErrorHandler : CommandErrorHandler {

        override fun handle(
                player: Player,
                commandLine: String,
                exception: Exception?
        ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.Processed

    }

}