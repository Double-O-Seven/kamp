package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IntCommandParameterResolverTest {

    private val intCommandParameterResolver = IntCommandParameterResolver()

    @Test
    fun shouldReturnIntJavaObjectTypeAsParameterType() {
        val parameterType = intCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Int::class.javaObjectType)
    }

    @Test
    fun shouldResolveIntString() {
        val value = intCommandParameterResolver.resolve("123")

        assertThat(value)
                .isEqualTo(123)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = intCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}