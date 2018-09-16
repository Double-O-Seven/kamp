package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LongCommandParameterResolverTest {

    private val longCommandParameterResolver = LongCommandParameterResolver()

    @Test
    fun shouldReturnLongJavaObjectTypeAsParameterType() {
        val parameterType = longCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(Long::class.javaObjectType)
    }

    @Test
    fun shouldResolveLongString() {
        val value = longCommandParameterResolver.resolve("123")

        assertThat(value)
                .isEqualTo(123L)
    }

    @Test
    fun givenNonNumericStringItShouldReturnNull() {
        val value = longCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isNull()
    }

}