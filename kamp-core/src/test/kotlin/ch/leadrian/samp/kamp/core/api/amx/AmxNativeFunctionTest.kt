package ch.leadrian.samp.kamp.core.api.amx

import ch.leadrian.samp.kamp.core.runtime.StringEncoding
import ch.leadrian.samp.kamp.core.runtime.amx.AmxNativeFunctionInvoker
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class AmxNativeFunctionTest {

    private val amxNativeFunctionInvoker: AmxNativeFunctionInvoker = mockk()

    @Test
    fun givenAllParameterTypeAreSimpleItShouldUseCallNative() {
        val address = 69
        every { amxNativeFunctionInvoker.findNative("test") } returns address
        every { amxNativeFunctionInvoker.callNative(address, 1337, 0.815f.toRawBits(), 1) } returns 999
        val amxNativeFunction = object : AmxNativeFunction(
                "test",
                amxNativeFunctionInvoker,
                IntType,
                FloatType,
                BooleanType
        ) {}

        val result = amxNativeFunction.invokeInternal(1337, 0.815f, true)

        assertThat(result)
                .isEqualTo(999)
    }

    @Test
    fun givenAtLeastOneComplexTypeItShouldUseInvokeNative() {
        val address = 69
        every { amxNativeFunctionInvoker.findNative("test") } returns address
        every {
            amxNativeFunctionInvoker.invokeNative(
                    address,
                    "isS[3]a[4]A[1]",
                    1337,
                    "Hahaha".toByteArray(StringEncoding.getCharset()),
                    "Abc".toByteArray(StringEncoding.getCharset()),
                    intArrayOf(1, 2, 3, 4),
                    intArrayOf(5)
            )
        } returns 999
        val amxNativeFunction = object : AmxNativeFunction(
                "test",
                amxNativeFunctionInvoker,
                IntType,
                StringType,
                OutputStringType,
                ImmutableCellArrayType,
                MutableCellArrayType
        ) {}

        val result = amxNativeFunction.invokeInternal(
                1337,
                "Hahaha",
                OutputString("Abc"),
                ImmutableCellArray(1, 2, 3, 4),
                MutableCellArray(intArrayOf(5))
        )

        assertThat(result)
                .isEqualTo(999)
    }

    @Test
    fun givenNativeCouldNotBeFoundItShouldThrowException() {
        every { amxNativeFunctionInvoker.findNative("test") } returns 0
        val amxNativeFunction = object : AmxNativeFunction(
                "test",
                amxNativeFunctionInvoker,
                IntType,
                FloatType,
                BooleanType
        ) {}

        val caughtThrowable = catchThrowable { amxNativeFunction.invokeInternal(1337, 0.815f, true) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Could not find native with name 'test'")
    }

}