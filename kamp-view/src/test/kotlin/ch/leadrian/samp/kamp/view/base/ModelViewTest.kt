package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
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

    @Nested
    inner class ZoomTests {

        @Test
        fun shouldBeInitializedWithOne() {
            val zoom = modelView.zoom

            assertThat(zoom)
                    .isOne()
        }

        @Test
        fun shouldSetZoom() {
            modelView.zoom = 0.5f

            assertThat(modelView.zoom)
                    .isEqualTo(0.5f)
        }

        @Test
        fun shouldSupplyZoom() {
            modelView.zoom { 0.5f }

            assertThat(modelView.zoom)
                    .isEqualTo(0.5f)
        }

    }

    @Nested
    inner class RotationTests {

        @Test
        fun shouldSetRotation() {
            modelView.rotation = vector3DOf(12.3f, 4.56f, 0.789f)

            assertThat(modelView.rotation)
                    .isEqualTo(vector3DOf(12.3f, 4.56f, 0.789f))
        }

        @Test
        fun shouldSupplyRotation() {
            modelView.rotation { vector3DOf(12.3f, 4.56f, 0.789f) }

            assertThat(modelView.rotation)
                    .isEqualTo(vector3DOf(12.3f, 4.56f, 0.789f))
        }

    }

    @Nested
    inner class VehicleColorsTests {

        @Test
        fun shouldInitiallyBeNull() {
            val vehicleColors = modelView.vehicleColors

            assertThat(vehicleColors)
                    .isNull()
        }

        @Test
        fun shouldSetVehicleColors() {
            modelView.vehicleColors = vehicleColorsOf(3, 6)

            assertThat(modelView.vehicleColors)
                    .isEqualTo(vehicleColorsOf(3, 6))
        }

        @Test
        fun shouldSupplyVehicleColors() {
            modelView.vehicleColors { vehicleColorsOf(3, 6) }

            assertThat(modelView.vehicleColors)
                    .isEqualTo(vehicleColorsOf(3, 6))
        }

    }

}