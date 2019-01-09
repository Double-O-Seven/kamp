package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale

internal class ListDialogTest {

    private lateinit var builder: ListDialog.Builder<Int>

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
        builder = ListDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)
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
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                item {
                    value(1337)
                    content("How are you?")
                }
            }.build()

            listDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.LIST.value,
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
            textProvider.apply {
                every { getText(locale, captionTextKey) } returns "Hi there"
                every { getText(locale, leftButtonTextKey) } returns "OK"
                every { getText(locale, rightButtonTextKey) } returns "Cancel"
            }
            val listDialog = builder.apply {
                caption(captionTextKey)
                leftButton(leftButtonTextKey)
                rightButton(rightButtonTextKey)
                item {
                    value(1337)
                    content("How are you?")
                }
            }.build()

            listDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.LIST.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithFunctionalDialogTextSuppliers() {
            val listDialog = builder.apply {
                caption { "Hi there" }
                leftButton { "OK" }
                rightButton { "Cancel" }
                item {
                    value(1337)
                    content("How are you?")
                }
            }.build()

            listDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.LIST.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithSuppliedDialogTextSuppliers() {
            val listDialog = builder.apply {
                caption(StringDialogTextSupplier("Hi there"))
                leftButton(StringDialogTextSupplier("OK"))
                rightButton(StringDialogTextSupplier("Cancel"))
                item {
                    value(1337)
                    content("How are you?")
                }
            }.build()

            listDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.LIST.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "How are you?"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithItems() {
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                item {
                    value(1337)
                    content("What a surprise!")
                }
                item(object : ListDialogItem<Int> {

                    override val value: Int = 69

                    override fun getContent(player: Player): String = "How are you?"

                    override fun onSelect(player: Player, inputText: String) {
                    }

                })
                items(
                        object : ListDialogItem<Int> {

                            override val value: Int = 187

                            override fun getContent(player: Player): String = "I'm good, what about you?"

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        },
                        object : ListDialogItem<Int> {

                            override val value: Int = 666

                            override fun getContent(player: Player): String =
                                    "To be honest, I wish I hadn't seen you so I wouldn't have to talk to your nasty face."

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        }
                )
                items(listOf(
                        object : ListDialogItem<Int> {

                            override val value: Int = 256

                            override fun getContent(player: Player): String = "Thanks for your honesty. I also really can't stand you."

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        },
                        object : ListDialogItem<Int> {

                            override val value: Int = 65536

                            override fun getContent(player: Player): String = "Great! I hope you die, have a shitty day!"

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        }
                ))
            }.build()

            listDialog.show(player)

            verify {
                nativeFunctionExecutor.showPlayerDialog(
                        dialogid = dialogId.value,
                        playerid = playerId.value,
                        style = DialogStyle.LIST.value,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        info = "What a surprise!\n" +
                                "How are you?\n" +
                                "I'm good, what about you?\n" +
                                "To be honest, I wish I hadn't seen you so I wouldn't have to talk to your nasty face.\n" +
                                "Thanks for your honesty. I also really can't stand you.\n" +
                                "Great! I hope you die, have a shitty day!"
                )
            }
        }
    }

    @Nested
    inner class OnResponseTests {

        private val onCancel = mockk<Dialog.(Player) -> OnDialogResponseListener.Result>()
        private val onSelectItem = mockk<Dialog.(Player, ListDialogItem<Int>, String) -> Unit>()

        @BeforeEach
        fun setUp() {
            every { onCancel.invoke(any(), any()) } returns OnDialogResponseListener.Result.Processed
            every { onSelectItem.invoke(any(), any(), any(), any()) } returns Unit
        }

        @Test
        fun givenResponseIsRightButtonItShouldCancel() {
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    content("How are you?")
                }
            }.build()

            val result = listDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "")

            verify {
                onCancel.invoke(listDialog, player)
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsRightButtonAndNoCancelCallbackItShouldDoNothing() {
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    content("How are you?")
                }
            }.build()

            val result = listDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "")

            verify {
                onCancel wasNot Called
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Ignored)
        }

        @Test
        fun givenResponseIsLeftButtonItShouldExecuteOnSelectAndOnSelectItem() {
            val onSelect = mockk<ListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    content("Hi there")
                }
                item {
                    value(69)
                    content("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    content("I'm fine")
                }
            }.build()

            val result = listDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 1, "Test")

            val slot = slot<ListDialogItem<Int>>()
            verify(exactly = 1) {
                onSelect(capture(slot), player, "Test")
            }
            assertThat(slot.captured)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(69)
                        assertThat(it.getContent(player))
                                .isEqualTo("How are you?")
                    }
            verify {
                onCancel wasNot Called
                onSelectItem.invoke(listDialog, player, slot.captured, "Test")
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsLeftButtonAndThereIsNoOnSelectItemItShouldExecuteOnSelectOfItem() {
            val onSelect = mockk<ListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                item {
                    value(1337)
                    content("Hi there")
                }
                item {
                    value(69)
                    content("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    content("I'm fine")
                }
            }.build()

            val result = listDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 1, "Test")

            val slot = slot<ListDialogItem<Int>>()
            verify(exactly = 1) {
                onSelect(capture(slot), player, "Test")
            }
            assertThat(slot.captured)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(69)
                        assertThat(it.getContent(player))
                                .isEqualTo("How are you?")
                    }
            verify {
                onCancel wasNot Called
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenInvalidItemIsClickedItShouldExecuteOnCancel() {
            val onSelect = mockk<ListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            every { player.name } returns "hans.wurst"
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    content("Hi there")
                }
                item {
                    value(69)
                    content("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    content("I'm fine")
                }
            }.build()

            val result = listDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 999, "Test")

            verify {
                onSelect wasNot Called
                onCancel.invoke(listDialog, player)
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenInvalidItemIsClickedAndThereIsNoOnCancelItShouldDoNothing() {
            val onSelect = mockk<ListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            every { player.name } returns "hans.wurst"
            val listDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    content("Hi there")
                }
                item {
                    value(69)
                    content("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    content("I'm fine")
                }
            }.build()

            val result = listDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 999, "Test")

            verify {
                onSelect wasNot Called
                onCancel wasNot Called
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Ignored)
        }
    }

}