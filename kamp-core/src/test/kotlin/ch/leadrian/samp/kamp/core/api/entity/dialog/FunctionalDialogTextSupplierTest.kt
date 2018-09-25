package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FunctionalDialogTextSupplierTest {

    @Test
    fun shouldGetText() {
        val player = mockk<Player>()
        val function = mockk<(Player) -> String> {
            every { this@mockk.invoke(player) } returns "hi there"
        }
        val dialogTextSupplier = FunctionalDialogTextSupplier(function)

        val text = dialogTextSupplier.getText(player)

        assertThat(text)
                .isEqualTo("hi there")
    }

}