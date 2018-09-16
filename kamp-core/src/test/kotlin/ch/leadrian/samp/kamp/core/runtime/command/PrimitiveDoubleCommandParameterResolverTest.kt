package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PrimitiveDoubleCommandParameterResolverTest {

    private val primitiveDoubleCommandParameterResolver = PrimitiveDoubleCommandParameterResolver()

    @Test
    fun shouldReturnDoubleJavaPrimitiveTypeAsParameterType() {
        val parameterType = primitiveDoubleCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Double::class.javaPrimitiveType!!)
    }

    @Test
    fun shouldResolveDoubleString() {
        val value = primitiveDoubleCommandParameterResolver.resolve("1.23")

        assertThat(value)
                .isEqualTo(1.23)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = primitiveDoubleCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}