package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class DialogRegistryTest {

    @Test
    fun shouldAllocateDialogIdForSingleDialog() {
        val dialog = TestDialog()
        val dialogRegistry = DialogRegistry(3)

        dialogRegistry.register { dialog.apply { id = it } }

        assertThat(dialog.id)
                .isEqualTo(DialogId.valueOf(0))
    }

    @Test
    fun shouldRegisterSingleDialog() {
        val dialog = TestDialog()
        val dialogRegistry = DialogRegistry(3)

        dialogRegistry.register { dialog.apply { id = it } }

        assertThat(dialogRegistry[DialogId.valueOf(0)])
                .isEqualTo(dialog)
    }

    @Test
    fun shouldAllocateDialogIdForMultipleDialogs() {
        val dialog1 = TestDialog()
        val dialog2 = TestDialog()
        val dialog3 = TestDialog()
        val dialogRegistry = DialogRegistry(3)

        dialogRegistry.register { dialog1.apply { id = it } }
        dialogRegistry.register { dialog2.apply { id = it } }
        dialogRegistry.register { dialog3.apply { id = it } }

        assertThat(dialog1.id)
                .isEqualTo(DialogId.valueOf(0))
        assertThat(dialog2.id)
                .isEqualTo(DialogId.valueOf(1))
        assertThat(dialog3.id)
                .isEqualTo(DialogId.valueOf(2))
    }

    @Test
    fun shouldRegisterMultipleDialogs() {
        val dialog1 = TestDialog()
        val dialog2 = TestDialog()
        val dialog3 = TestDialog()
        val dialogRegistry = DialogRegistry(3)

        dialogRegistry.register { dialog1.apply { id = it } }
        dialogRegistry.register { dialog2.apply { id = it } }
        dialogRegistry.register { dialog3.apply { id = it } }

        assertThat(dialogRegistry[DialogId.valueOf(0)])
                .isEqualTo(dialog1)
        assertThat(dialogRegistry[DialogId.valueOf(1)])
                .isEqualTo(dialog2)
        assertThat(dialogRegistry[DialogId.valueOf(2)])
                .isEqualTo(dialog3)
    }

    @Test
    fun givenCapacityIsReachedItShouldThrowAnException() {
        val dialog1 = TestDialog()
        val dialog2 = TestDialog()
        val dialog3 = TestDialog()
        val dialogRegistry = DialogRegistry(2)

        dialogRegistry.register { dialog1 }
        dialogRegistry.register { dialog2 }

        val caughtThrowable = catchThrowable { dialogRegistry.register { dialog3 } }

        assertThat(caughtThrowable)
                .isInstanceOf(NoSuchElementException::class.java)
    }

    private class TestDialog : AbstractDialog(DialogId.valueOf(0)) {

        override lateinit var id: DialogId

        override fun show(forPlayer: Player) = throw UnsupportedOperationException()

        override fun onResponse(player: Player, response: DialogResponse, listItem: Int, inputText: String) = throw UnsupportedOperationException()

    }

}