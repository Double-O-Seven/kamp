package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ViewContext
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Locale

internal class SpriteViewTest {

    private lateinit var spriteView: SpriteView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()

    @BeforeEach
    fun setUp() {
        spriteView = SpriteView(player, viewContext, playerTextDrawService)
    }

    @Nested
    inner class ColorTests {

        @Test
        fun shouldBeInitializedWithWhite() {
            val color = spriteView.color

            assertThat(color)
                    .isEqualTo(Colors.WHITE)
        }

        @Test
        fun shouldSetColor() {
            spriteView.color = Colors.PINK

            assertThat(spriteView.color)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            spriteView.color { Colors.PINK }

            assertThat(spriteView.color)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class SpriteNameTests {

        private val locale = Locale.GERMANY

        @BeforeEach
        fun setUp() {
            every { player.locale } returns locale
        }

        @Test
        fun shouldBeInitializedWithWhiteRectangle() {
            val spriteName = spriteView.spriteName

            assertThat(spriteName)
                    .isEqualTo("LD_SPAC:white")
        }

        @Test
        fun shouldSetSpriteName() {
            spriteView.spriteName = "ld_beat:left"

            assertThat(spriteView.spriteName)
                    .isEqualTo("ld_beat:left")
        }

        @Test
        fun shouldSupplySpriteName() {
            spriteView.spriteName { "ld_beat:left" }

            assertThat(spriteView.spriteName)
                    .isEqualTo("ld_beat:left")
        }

    }

}