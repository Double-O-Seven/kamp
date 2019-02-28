package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.amx.AmxCallbackParameterResolver
import ch.leadrian.samp.kamp.core.runtime.amx.AmxCallbackParameters
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

internal class AmxCallbackTest {

    @Test
    fun shouldCallOnPublicCall() {
        val parameters = AmxCallbackParameters(1337, 1234)
        val parameterValues = arrayOf(999, "haha")
        val amxCallbackParameterResolver = mockk<AmxCallbackParameterResolver> {
            every { resolve(listOf(Int::class, String::class), parameters) } returns parameterValues
        }
        val expectedResult = 1234
        val onPublicCall = mockk<(Array<Any>) -> Int> {
            every { this@mockk.invoke(parameterValues) } returns expectedResult
        }
        val amxCallback = TestAmxCallback(
                "onTest",
                amxCallbackParameterResolver,
                Int::class,
                String::class,
                onPublicCall = onPublicCall
        )

        val result = amxCallback.onPublicCall(parameters)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    private class TestAmxCallback(
            name: String,
            amxCallbackParameterResolver: AmxCallbackParameterResolver,
            vararg parameterTypes: KClass<*>,
            private val onPublicCall: (Array<Any>) -> Int
    ) : AmxCallback(name, amxCallbackParameterResolver, *parameterTypes) {

        override fun onPublicCall(parameterValues: Array<Any>): Int {
            return onPublicCall.invoke(parameterValues)
        }

    }

}