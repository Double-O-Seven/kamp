package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DoubleCommandParameterResolverTest {

    private val doubleCommandParameterResolver = DoubleCommandParameterResolver()

    @Test
    fun shouldReturnDoubleJavaObjectTypeAsParameterType() {
        val parameterType = doubleCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Double::class.javaObjectType)
    }

    @Test
    fun shouldResolveDoubleString() {
        val value = doubleCommandParameterResolver.resolve("1.23")

        assertThat(value)
                .isEqualTo(1.23)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = doubleCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}