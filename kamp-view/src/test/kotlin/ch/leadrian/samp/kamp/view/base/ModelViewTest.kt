package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ViewContext
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ModelViewTest {

    private lateinit var modelView: ModelView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()

    @BeforeEach
    fun setUp() {
        modelView = ModelView(player, viewContext, playerTextDrawService)
    }

    @Nested
    inner class ColorTests {

        @Test
        fun shouldBeInitializedWithWhite() {
            val color = modelView.color

            assertThat(color)
                    .isEqualTo(Colors.WHITE)
        }

        @Test
        fun shouldSetColor() {
            modelView.color = Colors.PINK

            assertThat(modelView.color)
                    .isEqualTo(Colors.PINK)
        }

        @Test
        fun shouldSupplyColor() {
            modelView.color { Colors.PINK }

            assertThat(modelView.color)
                    .isEqualTo(Colors.PINK)
        }

    }

    @Nested
    inner class ModelIdTests {

        @Test
        fun shouldSetModelId() {
            modelView.modelId = 1337

            assertThat(modelView.modelId)
                    .isEqualTo(1337)
        }

        @Test
        fun shouldSupplyModelId() {
            modelView.modelId { 1337 }

            assertThat(modelView.modelId)
                    .isEqualTo(1337)
        }

    }

}