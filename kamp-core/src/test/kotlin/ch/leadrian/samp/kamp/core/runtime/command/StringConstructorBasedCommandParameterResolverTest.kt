package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StringConstructorBasedCommandParameterResolverTest {

    @Test
    fun shouldResolveStringValueWithStringConstructor() {
        val commandParameterResolver = StringConstructorBasedCommandParameterResolver.forType(Foo::class.javaObjectType)!!

        val result = commandParameterResolver.resolve("123")

        assertThat(result)
                .isEqualTo(Foo("123"))
    }

    @Test
    fun givenTypeWithoutStringConstructorShouldReturnNull() {
        val commandParameterResolver = StringConstructorBasedCommandParameterResolver.forType(Bar::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    @Test
    fun givenTypeWithParameterlessConstructorShouldReturnNull() {
        val commandParameterResolver = StringConstructorBasedCommandParameterResolver.forType(Qux::class.java)

        assertThat(commandParameterResolver)
                .isNull()
    }

    data class Foo(val foo: String)

    data class Bar(val bar: Int)

    class Qux

}