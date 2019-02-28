package ch.leadrian.samp.kamp.core.runtime.amx

import ch.leadrian.samp.kamp.core.api.amx.AmxCallback
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class AmxCallbackExecutorTest {

    private val amxCallbackExecutor = AmxCallbackExecutor()

    @Test
    fun shouldCallCallback() {
        val paramsAddress = 1234
        val heapPointer = 69
        val callbackName = "onFoo"
        val expectedResult = 1337
        val amxCallback = mockk<AmxCallback> {
            every {
                onPublicCall(
                        AmxCallbackParameters(
                                paramsAddress,
                                heapPointer
                        )
                )
            } returns expectedResult
            every { name } returns callbackName
        }
        amxCallbackExecutor.register(amxCallback)

        val result = amxCallbackExecutor.onPublicCall(callbackName, paramsAddress, heapPointer)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Test
    fun givenNoRegisteredCallbackItShouldReturnNull() {
        val result = amxCallbackExecutor.onPublicCall("onBar", 1337, 69)

        assertThat(result)
                .isNull()
    }

    @Test
    fun shouldNotCallUnregisteredCallback() {
        val callbackName = "onBaz"
        val amxCallback = mockk<AmxCallback> {
            every { onPublicCall(any<AmxCallbackParameters>()) } returns 0
            every { name } returns callbackName
        }
        amxCallbackExecutor.register(amxCallback)
        amxCallbackExecutor.unregister(amxCallback)

        amxCallbackExecutor.onPublicCall(callbackName, 1337, 69)

        verify(exactly = 0) { amxCallback.onPublicCall(any<AmxCallbackParameters>()) }
    }

    @Test
    fun givenAnotherCallbackIsAlreadyRegisteredWithNameRegisterShouldThrowAnException() {
        val callbackName = "onQux"
        val amxCallback1 = mockk<AmxCallback> {
            every { name } returns callbackName
        }
        val amxCallback2 = mockk<AmxCallback> {
            every { name } returns callbackName
        }
        amxCallbackExecutor.register(amxCallback1)

        val caughtThrowable = catchThrowable { amxCallbackExecutor.register(amxCallback2) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Callback with name 'onQux' is already registered")
    }

    @Test
    fun givenAnotherCallbackIsAlreadyRegisteredWithNameUnregisterShouldThrowAnException() {
        val callbackName = "onFoobar"
        val amxCallback1 = mockk<AmxCallback> {
            every { name } returns callbackName
        }
        val amxCallback2 = mockk<AmxCallback> {
            every { name } returns callbackName
        }
        amxCallbackExecutor.register(amxCallback1)

        val caughtThrowable = catchThrowable { amxCallbackExecutor.unregister(amxCallback2) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Callback with name 'onFoobar' is not registered")
    }

}