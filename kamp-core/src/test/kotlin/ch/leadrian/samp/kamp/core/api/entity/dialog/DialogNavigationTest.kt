package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifySequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class DialogNavigationTest {

    private lateinit var dialogNavigation: DialogNavigation

    private val player = mockk<Player>()
    private val dialog = mockk<Dialog>()

    @BeforeEach
    fun setUp() {
        dialogNavigation = DialogNavigation(player)
        every { dialog.show(player) } just Runs
    }

    @Test
    fun givenEmptyDialogNavigationTopShouldBeNull() {
        val top = dialogNavigation.top

        assertThat(top)
                .isNull()
    }

    @Test
    fun givenEmptyDialogNavigationSizeShouldBeZero() {
        val size = dialogNavigation.size

        assertThat(size)
                .isZero()
    }

    @Test
    fun givenEmptyDialogNavigationItShouldBeEmpty() {
        val isEmpty = dialogNavigation.isEmpty

        assertThat(isEmpty)
                .isTrue()
    }

    @Nested
    inner class PushTests {

        @Test
        fun shouldShowDialog() {
            dialogNavigation.push(dialog)

            val slot = slot<Player>()
            verify(exactly = 1) { dialog.show(capture(slot)) }
            assertThat(slot.captured)
                    .isSameAs(player)
        }

        @Test
        fun givenPushItShouldNotBeEmpty() {
            dialogNavigation.push(dialog)

            val isEmpty = dialogNavigation.isEmpty

            assertThat(isEmpty)
                    .isFalse()
        }

        @Test
        fun givenNoOtherDialogsInNavigationItShouldPutCurrentDialogOnTop() {
            dialogNavigation.push(dialog)

            assertThat(dialogNavigation.size)
                    .isOne()
            assertThat(dialogNavigation.top)
                    .isSameAs(dialog)
        }

        @Test
        fun givenExistingDialogsInNavigationItShouldPutCurrentDialogOnTop() {
            val previousDialog1 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog2 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            dialogNavigation.push(previousDialog1)
            dialogNavigation.push(previousDialog2)

            dialogNavigation.push(dialog)

            assertThat(dialogNavigation.size)
                    .isEqualTo(3)
            assertThat(dialogNavigation.top)
                    .isSameAs(dialog)
            verifySequence {
                previousDialog1.show(player)
                previousDialog2.show(player)
                dialog.show(player)
            }
        }
    }

    @Nested
    inner class PopTests {

        @Test
        fun givenItIsEmptyItShouldNotDoAnything() {
            dialogNavigation.pop()

            verify { dialog wasNot Called }
        }

        @Test
        fun givenSingleElementTopShouldBeNull() {
            dialogNavigation.push(dialog)

            dialogNavigation.pop()

            assertThat(dialogNavigation.top)
                    .isNull()
        }

        @Test
        fun givenSingleElementSizeShouldBeZero() {
            dialogNavigation.push(dialog)

            dialogNavigation.pop()

            assertThat(dialogNavigation.size)
                    .isZero()
        }

        @Test
        fun givenSingleElementItShouldBeEmpty() {
            dialogNavigation.push(dialog)

            dialogNavigation.pop()

            assertThat(dialogNavigation.isEmpty)
                    .isTrue()
        }

        @Test
        fun givenMultipleElementsItShouldShowPreviousElementAndRemoveCurrent() {
            val previousDialog1 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog2 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            dialogNavigation.push(previousDialog1)
            dialogNavigation.push(previousDialog2)
            dialogNavigation.push(dialog)

            dialogNavigation.pop()

            assertThat(dialogNavigation.size)
                    .isEqualTo(2)
            assertThat(dialogNavigation.top)
                    .isSameAs(previousDialog2)
            verifySequence {
                previousDialog1.show(player)
                previousDialog2.show(player)
                dialog.show(player)
                previousDialog2.show(player)
            }
        }
    }

    @Nested
    inner class SetRootTests {

        @Test
        fun shouldShowDialog() {
            dialogNavigation.setRoot(dialog)

            val slot = slot<Player>()
            verify(exactly = 1) { dialog.show(capture(slot)) }
            assertThat(slot.captured)
                    .isSameAs(player)
        }

        @Test
        fun givenExistingDialogsInNavigationItShouldShowDialog() {
            val previousDialog1 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog2 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            dialogNavigation.push(previousDialog1)
            dialogNavigation.push(previousDialog2)

            dialogNavigation.setRoot(dialog)

            verifySequence {
                previousDialog1.show(player)
                previousDialog2.show(player)
                dialog.show(player)
            }
        }

        @Test
        fun givenExistingDialogsInNavigationItShouldSetDialogAsOnlyElement() {
            val previousDialog1 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog2 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog3 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            dialogNavigation.push(previousDialog1)
            dialogNavigation.push(previousDialog2)
            dialogNavigation.push(previousDialog3)

            dialogNavigation.setRoot(dialog)

            assertThat(dialogNavigation.size)
                    .isOne()
            assertThat(dialogNavigation.top)
                    .isSameAs(dialog)
        }

    }

    @Nested
    inner class ReplaceTopTests {

        @Test
        fun shouldShowDialog() {
            dialogNavigation.replaceTop(dialog)

            val slot = slot<Player>()
            verify(exactly = 1) { dialog.show(capture(slot)) }
            assertThat(slot.captured)
                    .isSameAs(player)
        }

        @Test
        fun givenPushItShouldNotBeEmpty() {
            dialogNavigation.replaceTop(dialog)

            val isEmpty = dialogNavigation.isEmpty

            assertThat(isEmpty)
                    .isFalse()
        }

        @Test
        fun givenNoOtherDialogsInNavigationItShouldPutCurrentDialogOnTop() {
            dialogNavigation.replaceTop(dialog)

            assertThat(dialogNavigation.size)
                    .isOne()
            assertThat(dialogNavigation.top)
                    .isSameAs(dialog)
        }

        @Test
        fun givenExistingDialogsInNavigationItShouldShowDialog() {
            val previousDialog1 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog2 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            dialogNavigation.push(previousDialog1)
            dialogNavigation.push(previousDialog2)

            dialogNavigation.replaceTop(dialog)

            verifySequence {
                previousDialog1.show(player)
                previousDialog2.show(player)
                dialog.show(player)
            }
        }

        @Test
        fun givenExistingDialogsInNavigationItShouldSetDialogAsOnlyElement() {
            val previousDialog1 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog2 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            val previousDialog3 = mockk<Dialog> {
                every { show(player) } just Runs
            }
            dialogNavigation.push(previousDialog1)
            dialogNavigation.push(previousDialog2)
            dialogNavigation.push(previousDialog3)

            dialogNavigation.replaceTop(dialog)

            assertThat(dialogNavigation.size)
                    .isEqualTo(3)
            assertThat(dialogNavigation.top)
                    .isSameAs(dialog)
        }

    }

    @Test
    fun clearShouldRemoveAllDialogs() {
        val previousDialog1 = mockk<Dialog> {
            every { show(player) } just Runs
        }
        val previousDialog2 = mockk<Dialog> {
            every { show(player) } just Runs
        }
        dialogNavigation.push(previousDialog1)
        dialogNavigation.push(previousDialog2)
        dialogNavigation.push(dialog)

        dialogNavigation.clear()

        assertThat(dialogNavigation.size)
                .isZero()
        assertThat(dialogNavigation.top)
                .isNull()
        assertThat(dialogNavigation.isEmpty)
                .isTrue()
    }

}