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
        val callbackName = "onFoo"
        val expectedResult = 1337
        val amxCallback = mockk<AmxCallback> {
            every { onPublicCall(paramsAddress) } returns expectedResult
            every { name } returns callbackName
        }
        amxCallbackExecutor.register(amxCallback)

        val result = amxCallbackExecutor.onPublicCall(callbackName, paramsAddress)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Test
    fun givenNoRegisteredCallbackItShouldReturnNull() {
        val result = amxCallbackExecutor.onPublicCall("onBar", 1337)

        assertThat(result)
                .isNull()
    }

    @Test
    fun shouldNotCallUnregisteredCallback() {
        val callbackName = "onBaz"
        val amxCallback = mockk<AmxCallback> {
            every { onPublicCall(any<Int>()) } returns 0
            every { name } returns callbackName
        }
        amxCallbackExecutor.register(amxCallback)
        amxCallbackExecutor.unregister(amxCallback)

        amxCallbackExecutor.onPublicCall(callbackName, 1337)

        verify(exactly = 0) { amxCallback.onPublicCall(any<Int>()) }
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
                .hasMessage("Callback with name 'onTest' is already registered")
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
                .hasMessage("Callback with name 'onTest' is not registered")
    }

}