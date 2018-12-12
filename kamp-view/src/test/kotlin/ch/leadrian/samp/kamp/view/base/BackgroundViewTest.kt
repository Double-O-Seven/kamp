package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ViewContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BackgroundViewTest {

    private lateinit var backgroundView: BackgroundView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()

    @BeforeEach
    fun setUp() {
        backgroundView = BackgroundView(player, viewContext, playerTextDrawService)
    }

    @Nested
    inner class ColorTests {

        @Test
        fun shouldBeInitializedWithTransparentBlack() {
            val color = backgroundView.color

            assertThat(color)
                    .isEqualTo(colorOf(0x00000080))
        }

        @Test
        fun shouldSetColor() {
            backgroundView.color = Colors.PINK

            assertThat(backgroundView.color)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            backgroundView.color { Colors.PINK }

            assertThat(backgroundView.color)
                    .isEqualTo(Colors.PINK)
        }

    }

}