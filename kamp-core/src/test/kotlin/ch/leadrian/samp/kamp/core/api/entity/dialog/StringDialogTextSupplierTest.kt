package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StringDialogTextSupplierTest {

    @Test
    fun shouldGetText() {
        val player = mockk<Player>()
        val dialogTextSupplier = StringDialogTextSupplier("hi there")

        val text = dialogTextSupplier.getText(player)

        assertThat(text)
                .isEqualTo("hi there")
    }

}