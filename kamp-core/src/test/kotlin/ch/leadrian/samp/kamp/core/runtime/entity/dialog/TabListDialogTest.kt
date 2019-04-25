package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
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

internal class TabListDialogTest {

    private lateinit var builder: TabListDialog.Builder<Int>

    private val textProvider = mockk<TextProvider>()
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
        builder = TabListDialog.Builder(textProvider, dialogRegistry)
    }

    @Nested
    inner class ShowTests {

        @BeforeEach
        fun setUp() {
            every { player.showDialog(any(), any(), any(), any(), any(), any()) } just Runs
        }

        @Test
        fun shouldBuildDialogWithStringDialogTextSuppliers() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                item {
                    value(1337)
                    tabbedContent("How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?\n"
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
            val tabListDialog = builder.apply {
                caption(captionTextKey)
                leftButton(leftButtonTextKey)
                rightButton(rightButtonTextKey)
                item {
                    value(1337)
                    tabbedContent("How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithFunctionalDialogTextSuppliers() {
            val tabListDialog = builder.apply {
                caption { "Hi there" }
                leftButton { "OK" }
                rightButton { "Cancel" }
                item {
                    value(1337)
                    tabbedContent("How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithSuppliedDialogTextSuppliers() {
            val tabListDialog = builder.apply {
                caption(StringDialogTextSupplier("Hi there"))
                leftButton(StringDialogTextSupplier("OK"))
                rightButton(StringDialogTextSupplier("Cancel"))
                item {
                    value(1337)
                    tabbedContent("How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "How are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithStringHeaderContentTextSupplier() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                headerContent("Question", "Answer")
                item {
                    value(1337)
                    tabbedContent("Hi there", "How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "Question\tAnswer\nHi there\tHow are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithTextKeyHeaderContentTextSupplier() {
            val textKey1 = TextKey("test.question")
            val textKey2 = TextKey("test.answer")
            every { textProvider.getText(locale, textKey1) } returns "Question"
            every { textProvider.getText(locale, textKey2) } returns "Answer"
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                headerContent(textKey1, textKey2)
                item {
                    value(1337)
                    tabbedContent("Hi there", "How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "Question\tAnswer\nHi there\tHow are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithFunctionalHeaderContentTextSupplier() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                headerContent({ "Question" }, { "Answer" })
                item {
                    value(1337)
                    tabbedContent("Hi there", "How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "Question\tAnswer\nHi there\tHow are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithSuppliedHeaderContentTextSupplier() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                headerContent(StringDialogTextSupplier("Question"), StringDialogTextSupplier("Answer"))
                item {
                    value(1337)
                    tabbedContent("Hi there", "How are you?")
                }
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST_HEADERS,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "Question\tAnswer\nHi there\tHow are you?\n"
                )
            }
        }

        @Test
        fun shouldBuildDialogWithItems() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                item {
                    value(1337)
                    tabbedContent("0:", "What a surprise!")
                }
                item(object : TabListDialogItem<Int> {

                    override val value: Int = 69

                    override fun getTabbedContent(player: Player): List<String> = listOf("1:", "How are you?")

                    override fun onSelect(player: Player, inputText: String) {
                    }

                })
                items(
                        object : TabListDialogItem<Int> {

                            override val value: Int = 187

                            override fun getTabbedContent(player: Player): List<String> =
                                    listOf("2:", "I'm good, what about you?")

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        },
                        object : TabListDialogItem<Int> {

                            override val value: Int = 666

                            override fun getTabbedContent(player: Player): List<String> =
                                    listOf(
                                            "3:",
                                            "To be honest, I wish I hadn't seen you so I wouldn't have to talk to your nasty face."
                                    )

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        }
                )
                items(listOf(
                        object : TabListDialogItem<Int> {

                            override val value: Int = 256

                            override fun getTabbedContent(player: Player): List<String> =
                                    listOf("4:", "Thanks for your honesty. I also really can't stand you.")

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        },
                        object : TabListDialogItem<Int> {

                            override val value: Int = 65536

                            override fun getTabbedContent(player: Player): List<String> =
                                    listOf("5:", "Great! I hope you die, have a shitty day!")

                            override fun onSelect(player: Player, inputText: String) {
                            }

                        }
                ))
            }.build()

            tabListDialog.show(player)

            verify {
                player.showDialog(
                        dialog = tabListDialog,
                        style = DialogStyle.TABLIST,
                        button1 = "OK",
                        button2 = "Cancel",
                        caption = "Hi there",
                        message = "0:\tWhat a surprise!\n" +
                                "1:\tHow are you?\n" +
                                "2:\tI'm good, what about you?\n" +
                                "3:\tTo be honest, I wish I hadn't seen you so I wouldn't have to talk to your nasty face.\n" +
                                "4:\tThanks for your honesty. I also really can't stand you.\n" +
                                "5:\tGreat! I hope you die, have a shitty day!\n"
                )
            }
        }
    }

    @Nested
    inner class OnResponseTests {

        private val onCancel = mockk<Dialog.(Player) -> OnDialogResponseListener.Result>()
        private val onSelectItem = mockk<Dialog.(Player, TabListDialogItem<Int>, String) -> Unit>()

        @BeforeEach
        fun setUp() {
            every { onCancel.invoke(any(), any()) } returns OnDialogResponseListener.Result.Processed
            every { onSelectItem.invoke(any(), any(), any(), any()) } returns Unit
        }

        @Test
        fun givenResponseIsRightButtonItShouldCancel() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    tabbedContent("How are you?")
                }
            }.build()

            val result = tabListDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "")

            verify {
                onCancel.invoke(tabListDialog, player)
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsRightButtonAndNoCancelCallbackItShouldDoNothing() {
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    tabbedContent("How are you?")
                }
            }.build()

            val result = tabListDialog.onResponse(player, DialogResponse.RIGHT_BUTTON, 0, "")

            verify {
                onCancel wasNot Called
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Ignored)
        }

        @Test
        fun givenResponseIsLeftButtonItShouldExecuteOnSelectAndOnSelectItem() {
            val onSelect = mockk<TabListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    tabbedContent("Hi there")
                }
                item {
                    value(69)
                    tabbedContent("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    tabbedContent("I'm fine")
                }
            }.build()

            val result = tabListDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 1, "Test")

            val slot = slot<TabListDialogItem<Int>>()
            verify(exactly = 1) {
                onSelect(capture(slot), player, "Test")
            }
            assertThat(slot.captured)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(69)
                        assertThat(it.getTabbedContent(player))
                                .isEqualTo(listOf("How are you?"))
                    }
            verify {
                onCancel wasNot Called
                onSelectItem.invoke(tabListDialog, player, slot.captured, "Test")
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenResponseIsLeftButtonAndThereIsNoOnSelectItemItShouldExecuteOnSelectOfItem() {
            val onSelect = mockk<TabListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                item {
                    value(1337)
                    tabbedContent("Hi there")
                }
                item {
                    value(69)
                    tabbedContent("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    tabbedContent("I'm fine")
                }
            }.build()

            val result = tabListDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 1, "Test")

            val slot = slot<TabListDialogItem<Int>>()
            verify(exactly = 1) {
                onSelect(capture(slot), player, "Test")
            }
            assertThat(slot.captured)
                    .satisfies {
                        assertThat(it.value)
                                .isEqualTo(69)
                        assertThat(it.getTabbedContent(player))
                                .isEqualTo(listOf("How are you?"))
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
            val onSelect = mockk<TabListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            every { player.name } returns "hans.wurst"
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onCancel(onCancel)
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    tabbedContent("Hi there")
                }
                item {
                    value(69)
                    tabbedContent("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    tabbedContent("I'm fine")
                }
            }.build()

            val result = tabListDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 999, "Test")

            verify {
                onSelect wasNot Called
                onCancel.invoke(tabListDialog, player)
                onSelectItem wasNot Called
            }
            assertThat(result)
                    .isEqualTo(OnDialogResponseListener.Result.Processed)
        }

        @Test
        fun givenInvalidItemIsClickedAndThereIsNoOnCancelItShouldDoNothing() {
            val onSelect = mockk<TabListDialogItem<Int>.(Player, String) -> Unit> {
                every { this@mockk.invoke(any(), any(), any()) } just Runs
            }
            every { player.name } returns "hans.wurst"
            val tabListDialog = builder.apply {
                caption("Hi there")
                leftButton("OK")
                rightButton("Cancel")
                onSelectItem(onSelectItem)
                item {
                    value(1337)
                    tabbedContent("Hi there")
                }
                item {
                    value(69)
                    tabbedContent("How are you?")
                    onSelect(onSelect)
                }
                item {
                    value(187)
                    tabbedContent("I'm fine")
                }
            }.build()

            val result = tabListDialog.onResponse(player, DialogResponse.LEFT_BUTTON, 999, "Test")

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