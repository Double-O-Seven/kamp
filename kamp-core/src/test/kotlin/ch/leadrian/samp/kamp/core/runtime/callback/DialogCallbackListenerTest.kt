package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogNavigation
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.EnumSource
import java.util.stream.Stream

internal class DialogCallbackListenerTest {

    private lateinit var dialogCallbackListener: DialogCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val dialogRegistry = mockk<DialogRegistry>()

    @BeforeEach
    fun setUp() {
        dialogCallbackListener = DialogCallbackListener(callbackListenerManager, dialogRegistry)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        dialogCallbackListener.initialize()

        verify { callbackListenerManager.register(dialogCallbackListener) }
    }

    @Nested
    inner class OnDialogResponseTests {

        private val player = mockk<Player>()

        @BeforeEach
        fun setUp() {
            every { player.resetCurrentDialog() } just Runs
        }

        @ParameterizedTest
        @EnumSource(DialogResponse::class)
        fun shouldResetPlayersCurrentDialog(response: DialogResponse) {
            val dialog = mockk<AbstractDialog> {
                every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Processed
            }
            val dialogId = DialogId.valueOf(1337)
            every { dialogRegistry[dialogId] } returns dialog

            dialogCallbackListener.onDialogResponse(
                    player,
                    dialogId,
                    response,
                    1337,
                    "Hi there"
            )

            verify { player.resetCurrentDialog() }
        }

        @Nested
        inner class LeftButtonResponseTests {

            @Test
            fun givenUnknownDialogIdItShouldReturnIgnored() {
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns null

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.LEFT_BUTTON,
                        1337,
                        "Hi there"
                )

                assertThat(result)
                        .isEqualTo(OnDialogResponseListener.Result.Ignored)
            }

            @Test
            fun shouldCallOnResponse() {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Processed
                }
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns dialog

                dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.LEFT_BUTTON,
                        1337,
                        "Hi there"
                )

                verify {
                    dialog.onResponse(player, DialogResponse.LEFT_BUTTON, 1337, "Hi there")
                }
            }

            @ParameterizedTest
            @ArgumentsSource(OnDialogResponseResultArgumentsProvider::class)
            fun givenAnyResultItShouldReturnResult(expectedResult: OnDialogResponseListener.Result) {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns expectedResult
                }
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns dialog

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.LEFT_BUTTON,
                        1337,
                        "Hi there"
                )

                assertThat(result)
                        .isEqualTo(expectedResult)
            }
        }

        @Nested
        inner class RightButtonResponseTests {

            @Test
            fun givenUnknownDialogIdItShouldReturnIgnored() {
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns null

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.RIGHT_BUTTON,
                        1337,
                        "Hi there"
                )

                assertThat(result)
                        .isEqualTo(OnDialogResponseListener.Result.Ignored)
            }

            @Test
            fun shouldCallOnResponse() {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Processed
                }
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns dialog

                dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.RIGHT_BUTTON,
                        1337,
                        "Hi there"
                )

                verify {
                    dialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 1337, "Hi there")
                }
            }

            @Test
            fun givenResultProcessedItShouldReturnProcessed() {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Processed
                }
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns dialog

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.RIGHT_BUTTON,
                        1337,
                        "Hi there"
                )

                assertThat(result)
                        .isEqualTo(OnDialogResponseListener.Result.Processed)
            }

            @Test
            fun givenResultIgnoredAndDialogNavigationTopIsNullItShouldReturnIgnored() {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Ignored
                }
                val dialogId = DialogId.valueOf(1337)
                val dialogNavigation = mockk<DialogNavigation> {
                    every { top } returns null
                }
                every { player.dialogNavigation } returns dialogNavigation
                every { dialogRegistry[dialogId] } returns dialog

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.RIGHT_BUTTON,
                        1337,
                        "Hi there"
                )

                assertThat(result)
                        .isEqualTo(OnDialogResponseListener.Result.Ignored)
            }

            @Test
            fun givenResultIgnoredAndDialogNavigationTopIsAnotherDialogItShouldReturnIgnored() {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Ignored
                }
                val otherDialog = mockk<AbstractDialog>()
                val dialogId = DialogId.valueOf(1337)
                val dialogNavigation = mockk<DialogNavigation> {
                    every { top } returns otherDialog
                }
                every { player.dialogNavigation } returns dialogNavigation
                every { dialogRegistry[dialogId] } returns dialog

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.RIGHT_BUTTON,
                        1337,
                        "Hi there"
                )

                assertThat(result)
                        .isEqualTo(OnDialogResponseListener.Result.Ignored)
            }

            @Test
            fun givenResultIgnoredAndDialogIsNavigationTopItShouldReturnPopNavigationAndReturnProcessed() {
                val dialog = mockk<AbstractDialog> {
                    every { onResponse(any(), any(), any(), any()) } returns OnDialogResponseListener.Result.Ignored
                }
                val dialogId = DialogId.valueOf(1337)
                val dialogNavigation = mockk<DialogNavigation> {
                    every { top } returns dialog
                    every { pop() } just Runs
                }
                every { player.dialogNavigation } returns dialogNavigation
                every { dialogRegistry[dialogId] } returns dialog

                val result = dialogCallbackListener.onDialogResponse(
                        player,
                        dialogId,
                        DialogResponse.RIGHT_BUTTON,
                        1337,
                        "Hi there"
                )

                verify { dialogNavigation.pop() }
                assertThat(result)
                        .isEqualTo(OnDialogResponseListener.Result.Processed)
            }
        }
    }

    private class OnDialogResponseResultArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
                Stream.of(
                        Arguments.of(OnDialogResponseListener.Result.Processed),
                        Arguments.of(OnDialogResponseListener.Result.Ignored)
                )

    }

}