package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CommandParameterResolverFactoryTest {

    private lateinit var commandParameterResolverFactory: CommandParameterResolverFactory

    private val commandParameterResolverRegistry = mockk<CommandParameterResolverRegistry>()

    @BeforeEach
    fun setUp() {
        commandParameterResolverFactory = CommandParameterResolverFactory(commandParameterResolverRegistry)
    }

    @Test
    fun shouldGetResolverFromRegistry() {
        val expectedResolver = mockk<CommandParameterResolver<Foo>>()
        every { commandParameterResolverRegistry.getResolver(Foo::class.java) } returns expectedResolver

        val resolver = commandParameterResolverFactory.getResolver(Foo::class.java)

        assertThat(resolver)
                .isSameAs(expectedResolver)
    }

    @Test
    fun givenNoMatchingResolverItShouldThrowException() {
        every { commandParameterResolverRegistry.getResolver(any<Class<*>>()) } returns null

        val caughtThrowable = catchThrowable { commandParameterResolverFactory.getResolver(Foo::class.java) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("No command parameter resolver for type ch.leadrian.samp.kamp.core.runtime.command.CommandParameterResolverFactoryTest\$Foo")
    }

    @Nested
    inner class StringConstructorResolverTests {

        @BeforeEach
        fun setUp() {
            every { commandParameterResolverRegistry.getResolver(any<Class<*>>()) } returns null
            every { commandParameterResolverRegistry.register(any()) } just Runs
        }

        @Test
        fun givenTypeWithStringConstructorItShouldReturnStringConstructorBasedResolver() {
            val resolver = commandParameterResolverFactory.getResolver(TypeWithStringConstructor::class.java)

            assertThat(resolver)
                    .isInstanceOf(StringConstructorBasedCommandParameterResolver::class.java)
        }

        @Test
        fun givenTypeWithStringConstructorItShouldRegisterResolver() {
            val resolver = commandParameterResolverFactory.getResolver(TypeWithStringConstructor::class.java)

            verify { commandParameterResolverRegistry.register(resolver) }
        }
    }

    @Nested
    inner class FactoryMethodResolverTests {

        @BeforeEach
        fun setUp() {
            every { commandParameterResolverRegistry.getResolver(any<Class<*>>()) } returns null
            every { commandParameterResolverRegistry.register(any()) } just Runs
        }

        @Test
        fun givenTypeWithFactoryMethodItShouldReturnFactoryMethodBasedResolver() {
            val resolver = commandParameterResolverFactory.getResolver(TypeWithFactoryMethod::class.java)

            assertThat(resolver)
                    .isInstanceOf(FactoryMethodBasedCommandParameterResolver::class.java)
        }

        @Test
        fun givenTypeWithFactoryMethodItShouldRegisterResolver() {
            val resolver = commandParameterResolverFactory.getResolver(TypeWithFactoryMethod::class.java)

            verify { commandParameterResolverRegistry.register(resolver) }
        }
    }

    class Foo

    class TypeWithStringConstructor(val value: String)

    class TypeWithFactoryMethod {

        companion object {

            @JvmStatic
            fun valueOf(@Suppress("UNUSED_PARAMETER") value: String): TypeWithFactoryMethod = TypeWithFactoryMethod()
        }

    }

}