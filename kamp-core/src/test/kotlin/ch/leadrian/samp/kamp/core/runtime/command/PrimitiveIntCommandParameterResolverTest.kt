package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PrimitiveIntCommandParameterResolverTest {

    private val primitiveIntCommandParameterResolver = PrimitiveIntCommandParameterResolver()

    @Test
    fun shouldReturnIntJavaPrimitiveTypeAsParameterType() {
        val parameterType = primitiveIntCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Int::class.javaPrimitiveType!!)
    }

    @Test
    fun shouldResolveIntString() {
        val value = primitiveIntCommandParameterResolver.resolve("123")

        assertThat(value)
                .isEqualTo(123)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = primitiveIntCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}