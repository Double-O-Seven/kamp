package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PrimitiveFloatCommandParameterResolverTest {

    private val primitiveFloatCommandParameterResolver = PrimitiveFloatCommandParameterResolver()

    @Test
    fun shouldReturnFloatJavaPrimitiveTypeAsParameterType() {
        val parameterType = primitiveFloatCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Float::class.javaPrimitiveType!!)
    }

    @Test
    fun shouldResolveFloatString() {
        val value = primitiveFloatCommandParameterResolver.resolve("1.23")

        assertThat(value)
                .isEqualTo(1.23f)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = primitiveFloatCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}