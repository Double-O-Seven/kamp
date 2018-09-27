package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OnDialogResponseHandlerTest {

    private val onDialogResponseHandler = OnDialogResponseHandler()
    private val player = mockk<Player>()
    private val dialogId = DialogId.valueOf(1337)
    private val response = DialogResponse.LEFT_BUTTON
    private val listItem = 69
    private val inputText = "Hi there"

    @Test
    fun givenNoListenerItShouldReturnIgnoredResult() {
        val result = onDialogResponseHandler.onDialogResponse(player, dialogId, response, listItem, inputText)

        assertThat(result)
                .isEqualTo(OnDialogResponseListener.Result.Ignored)
    }

    @Test
    fun givenAllListenersReturnIgnoredItShouldReturnIgnored() {
        val listener1 = mockk<OnDialogResponseListener> {
            every {
                onDialogResponse(player, dialogId, response, listItem, inputText)
            } returns OnDialogResponseListener.Result.Ignored
        }
        val listener2 = mockk<OnDialogResponseListener> {
            every {
                onDialogResponse(player, dialogId, response, listItem, inputText)
            } returns OnDialogResponseListener.Result.Ignored
        }
        val listener3 = mockk<OnDialogResponseListener> {
            every {
                onDialogResponse(player, dialogId, response, listItem, inputText)
            } returns OnDialogResponseListener.Result.Ignored
        }
        val onDialogResponseHandler = OnDialogResponseHandler()
        onDialogResponseHandler.register(listener1)
        onDialogResponseHandler.register(listener2)
        onDialogResponseHandler.register(listener3)

        val result = onDialogResponseHandler.onDialogResponse(player, dialogId, response, listItem, inputText)

        verify(exactly = 1) {
            listener1.onDialogResponse(player, dialogId, response, listItem, inputText)
            listener2.onDialogResponse(player, dialogId, response, listItem, inputText)
            listener3.onDialogResponse(player, dialogId, response, listItem, inputText)
        }
        assertThat(result)
                .isEqualTo(OnDialogResponseListener.Result.Ignored)
    }

    @Test
    fun shouldStopWithFirstProcessedResult() {
        val listener1 = mockk<OnDialogResponseListener> {
            every { onDialogResponse(player, dialogId, response, listItem, inputText) } returns OnDialogResponseListener.Result.Ignored
        }
        val listener2 = mockk<OnDialogResponseListener> {
            every { onDialogResponse(player, dialogId, response, listItem, inputText) } returns OnDialogResponseListener.Result.Processed
        }
        val listener3 = mockk<OnDialogResponseListener>()
        val onDialogResponseHandler = OnDialogResponseHandler()
        onDialogResponseHandler.register(listener1)
        onDialogResponseHandler.register(listener2)
        onDialogResponseHandler.register(listener3)

        val result = onDialogResponseHandler.onDialogResponse(player, dialogId, response, listItem, inputText)

        verify(exactly = 1) {
            listener1.onDialogResponse(player, dialogId, response, listItem, inputText)
            listener2.onDialogResponse(player, dialogId, response, listItem, inputText)
        }
        verify { listener3 wasNot Called }
        assertThat(result)
                .isEqualTo(OnDialogResponseListener.Result.Processed)
    }
}