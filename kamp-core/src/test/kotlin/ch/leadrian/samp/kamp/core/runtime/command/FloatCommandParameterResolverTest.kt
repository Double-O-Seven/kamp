package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FloatCommandParameterResolverTest {

    private val floatCommandParameterResolver = FloatCommandParameterResolver()

    @Test
    fun shouldReturnFloatJavaObjectTypeAsParameterType() {
        val parameterType = floatCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Float::class.javaObjectType)
    }

    @Test
    fun shouldResolveFloatString() {
        val value = floatCommandParameterResolver.resolve("1.23")

        assertThat(value)
                .isEqualTo(1.23f)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = floatCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}