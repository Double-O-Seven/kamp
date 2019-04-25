package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.entity.dialog.InputDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.InputDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.ListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.MessageBoxDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.TabListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DialogServiceTest {

    private lateinit var dialogService: DialogService

    private val textProvider = mockk<TextProvider>()
    private val dialogRegistry = mockk<DialogRegistry>()

    @BeforeEach
    fun setUp() {
        dialogService = DialogService(textProvider, dialogRegistry)
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

}