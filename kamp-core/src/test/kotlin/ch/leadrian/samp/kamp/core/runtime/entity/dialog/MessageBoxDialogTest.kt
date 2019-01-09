package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale

internal class MessageBoxDialogTest {

    private lateinit var builder: MessageBoxDialog.Builder

    private val textProvider = mockk<TextProvider>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val dialogRegistry = mockk<DialogRegistry>()
    private val playerId = PlayerId.valueOf(50)
    private val player = mockk<Player>()
    private val locale = Locale.GERMANY
    private val dialogId = DialogId.valueOf(69)

    @BeforeEach
    fun setUp() {
        every { dialogRegistry.register<AbstractDialog>(any()) } answers {
            firstArg<(DialogId) -> AbstractDialog>().invoke(dialogId)
        }
        every { player.locale } returns locale
        every { player.id } returns playerId
        builder = MessageBoxDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)
    }

    @Nested
    inner class ShowTests {

        @BeforeEach
        fun setUp() {
            every {
                nativeFunctionExecutor.showPlayerDialog(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                )
            } returns true
        }

        @Test
        fun shouldBuildDialogWithStringDialogTextSuppliers() {
            val messageBoxDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
            }.build()

            messageBoxDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.MSGBOX.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithTextKeyDialogTextSuppliers() {
            val captionTextKey = TextKey("test.caption")
            val leftButtonTextKey = TextKey("test.left.button")
            val rightButtonTextKey = TextKey("test.right.button")
            val messageBoxTextKey = TextKey("test.messageBox")
            textProvider.apply {
                every { getText(locale, captionTextKey) } returns "Hi there"
                every { getText(locale, leftButtonTextKey) } returns "OK"
                every { getText(locale, rightButtonTextKey) } returns "Cancel"
                every { getText(locale, messageBoxTextKey) } returns "How are you?"
            }
            val messageBoxDialog = builder.apply {
                caption(captionTextKey)
                leftButton(leftButtonTextKey)
                rightButton(rightButtonTextKey)
                message(messageBoxTextKey)
            }.build()

            messageBoxDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.MSGBOX.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithFunctionalDialogTextSuppliers() {
            val messageBoxDialog = builder.apply {
                caption { "Hi there" }
                leftButton { "OK" }
                rightButton { "Cancel" }
                message { "How are you?" }
            }.build()

            messageBoxDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.MSGBOX.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithSuppliedDialogTextSuppliers() {
            val messageBoxDialog = builder.apply {
                caption(StringDialogTextSupplier("Hi there"))
                leftButton(StringDialogTextSupplier("OK"))
                rightButton(StringDialogTextSupplier("Cancel"))
                message(StringDialogTextSupplier("How are you?"))
            }.build()

            messageBoxDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.MSGBOX.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }
    }

    @Nested
    inner class OnResponseTests {

        private val onClickRightButton = mockk<Dialog.(Player) -> OnDialogResponseListener.Result>()
        private val onClickLeftButton = mockk<Dialog.(Player) -> Unit>()

        @BeforeEach
        fun setUp() {
            every { onClickRightButton.invoke(any(), any()) } returns OnDialogResponseListener.Result.Processed
            every { onClickLeftButton.invoke(any(), any()) } returns Unit
        }

        @Test
        fun givenResponseIsRightButtonItShouldCallOnClickRightButton() {
            val messageBoxDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onClickRightButton(onClickRightButton)
                onClickLeftButton(onClickLeftButton)
            }.build()

            val result = messageBoxDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "")

            verify {
                onClickRightButton.invoke(messageBoxDialog, player)
                onClickLeftButton wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsRightButtonAndNoOnClickRightButtonItShouldDoNothing() {
            val messageBoxDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onClickLeftButton(onClickLeftButton)
            }.build()

            val result = messageBoxDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "")

            verify {
                onClickRightButton wasNot Called
                onClickLeftButton wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Ignored)
        }

        @Test
        fun givenResponseIsLeftButtonAndNoOnClickLeftButtonItShouldDoNothing() {
            val messageBoxDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                message("How are you?")
                onClickRightButton(onClickRightButton)
            }.build()

            val result = messageBoxDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 0, "")

            verify {
                onClickRightButton wasNot Called
                onClickLeftButton wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }
    }

}