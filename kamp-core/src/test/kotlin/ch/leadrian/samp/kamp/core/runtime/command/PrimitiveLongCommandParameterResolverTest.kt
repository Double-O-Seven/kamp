package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PrimitiveLongCommandParameterResolverTest {

    private val primitiveLongCommandParameterResolver = PrimitiveLongCommandParameterResolver()

    @Test
    fun shouldReturnLongJavaPrimitiveTypeAsParameterType() {
        val parameterType = primitiveLongCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Long::class.javaPrimitiveType!!)
    }

    @Test
    fun shouldResolveLongString() {
        val value = primitiveLongCommandParameterResolver.resolve("123")

        assertThat(value)
                .isEqualTo(123L)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = primitiveLongCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}