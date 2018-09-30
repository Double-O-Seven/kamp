package ch.leadrian.samp.kamp.core.runtime.service

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogNavigation
import ch.leadrian.samp.kamp.core.api.entity.dialog.InputDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.InputDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.ListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.MessageBoxDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.TabListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
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
import java.util.stream.Stream

internal class DialogServiceImplTest {

    private lateinit var dialogService: DialogServiceImpl

    private val textProvider = mockk<TextProvider>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val dialogRegistry = mockk<DialogRegistry>()

    @BeforeEach
    fun setUp() {
        dialogService = DialogServiceImpl(textProvider, nativeFunctionExecutor, callbackListenerManager, dialogRegistry)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        dialogService.initialize()

        verify { callbackListenerManager.register(dialogService) }
    }

    @Nested
    inner class NewBuilderTests {

        @Test
        fun shouldReturnInputDialogBuilder() {
            val builder = dialogService.newInputDialogBuilder()

            assertThat(builder)
                    .isInstanceOf(InputDialogBuilder::class.java)
        }

        @Test
        fun shouldReturnMessageBoxDialogBuilder() {
            val builder = dialogService.newMessageBoxDialogBuilder()

            assertThat(builder)
                    .isInstanceOf(MessageBoxDialogBuilder::class.java)
        }

        @Test
        fun shouldReturnListDialogBuilder() {
            val builder = dialogService.newListDialogBuilder<Any>()

            assertThat(builder)
                    .isInstanceOf(ListDialogBuilder::class.java)
        }

        @Test
        fun shouldReturnTabListDialogBuilder() {
            val builder = dialogService.newTabListDialogBuilder<Any>()

            assertThat(builder)
                    .isInstanceOf(TabListDialogBuilder::class.java)
        }

    }

    @Nested
    inner class CreateDialogTests {

        @BeforeEach
        fun setUp() {
            every { dialogRegistry.register<AbstractDialog>(any()) } answers {
                firstArg<(DialogId) -> AbstractDialog>().invoke(DialogId.valueOf(1234))
            }
        }

        @Test
        fun shouldReturnInputDialog() {
            val builderBlock = spyk<InputDialogBuilder.() -> Unit>(objToCopy = {
                caption("Hi")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
            })

            val inputDialog = dialogService.createInputDialog(builderBlock)

            assertThat(inputDialog)
                    .isInstanceOf(InputDialog::class.java)
            verify(exactly = 1) { builderBlock.invoke(any()) }
        }

        @Test
        fun shouldReturnMessageBoxDialog() {
            val builderBlock = spyk<MessageBoxDialogBuilder.() -> Unit>(objToCopy = {
                caption("Hi")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
            })

            val messageBoxDialog = dialogService.createMessageBoxDialog(builderBlock)

            assertThat(messageBoxDialog)
                    .isInstanceOf(MessageBoxDialog::class.java)
            verify(exactly = 1) { builderBlock.invoke(any()) }
        }

        @Test
        fun shouldReturnListDialog() {
            val builderBlock = spyk<ListDialogBuilder<Any>.() -> Unit>(objToCopy = {
                caption("Hi")
                leftButton("OK")
                rightButton("Cancel")
            })

            val listDialog = dialogService.createListDialog(builderBlock)

            assertThat(listDialog)
                    .isInstanceOf(ListDialog::class.java)
            verify(exactly = 1) { builderBlock.invoke(any()) }
        }

        @Test
        fun shouldReturnTabListDialog() {
            val builderBlock = spyk<TabListDialogBuilder<Any>.() -> Unit>(objToCopy = {
                caption("Hi")
                leftButton("OK")
                rightButton("Cancel")
            })

            val tabListDialog = dialogService.createTabListDialog(builderBlock)

            assertThat(tabListDialog)
                    .isInstanceOf(TabListDialog::class.java)
            verify(exactly = 1) { builderBlock.invoke(any()) }
        }
    }

    @Nested
    inner class OnDialogResponseTests {

        private val player = mockk<Player>()

        @Nested
        inner class LeftButtonResponseTests {

            @Test
            fun givenUnknownDialogIdItShouldReturnIgnored() {
                val dialogId = DialogId.valueOf(1337)
                every { dialogRegistry[dialogId] } returns null

                val result = dialogService.onDialogResponse(
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

                dialogService.onDialogResponse(
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

                val result = dialogService.onDialogResponse(
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

                val result = dialogService.onDialogResponse(
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

                dialogService.onDialogResponse(
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

                val result = dialogService.onDialogResponse(
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

                val result = dialogService.onDialogResponse(
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

                val result = dialogService.onDialogResponse(
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

                val result = dialogService.onDialogResponse(
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