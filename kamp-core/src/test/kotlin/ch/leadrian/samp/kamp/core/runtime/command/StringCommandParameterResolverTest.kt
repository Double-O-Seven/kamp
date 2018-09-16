package ch.leadrian.samp.kamp.core.runtime.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StringCommandParameterResolverTest {

    private val stringCommandParameterResolver = StringCommandParameterResolver()

    @Test
    fun shouldReturnStringAsParameterType() {
        val parameterType = stringCommandParameterResolver.parameterType

        assertThat(parameterType)
                .isEqualTo(String::class.java)
    }

    @Test
    fun shouldResolveValueToItself() {
        val value = stringCommandParameterResolver.resolve("hahaha")

        assertThat(value)
                .isEqualTo("hahaha")
    }
}