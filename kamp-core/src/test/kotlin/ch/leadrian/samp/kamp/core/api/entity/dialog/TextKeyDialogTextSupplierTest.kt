package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class TextKeyDialogTextSupplierTest {

    @Test
    fun shouldGetText() {
        val locale = Locale.GERMANY
        val player = mockk<Player> {
            every { this@mockk.locale } returns locale
        }
        val textKey = TextKey("hi.there")
        val textProvider = mockk<TextProvider> {
            every { getText(locale, textKey) } returns "Hi there!"
        }
        val dialogTextSupplier = TextKeyDialogTextSupplier(textKey, textProvider)

        val text = dialogTextSupplier.getText(player)

        assertThat(text)
                .isEqualTo("Hi there!")
    }

}