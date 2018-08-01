package ch.leadrian.samp.kamp.api.text

import ch.leadrian.samp.kamp.api.data.colorOf
import ch.leadrian.samp.kamp.api.entity.Player
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class MessageArgumentsTest {

    @Test
    fun shouldReturnEmbeddedPlayerName() {
        val player = mockk<Player> {
            every { color } returns colorOf(0x00FF00FF)
            every { name } returns "hans_wurst"
        }
        val embeddedPlayerName = embeddedPlayerNameOf(player)

        val text = embeddedPlayerName.get(Locale.GERMANY, colorOf(0x33CC33FF))

        assertThat(text)
                .isEqualTo("{00ff00}hans_wurst{33cc33}")
    }

}