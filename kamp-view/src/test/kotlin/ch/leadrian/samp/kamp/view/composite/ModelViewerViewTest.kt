package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.factory.DefaultViewFactory
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ModelViewerViewTest {

    private lateinit var modelViewerView: ModelViewerView
    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()
    private val playerTextDrawService = mockk<PlayerTextDrawService>()
    private val dialogService = mockk<DialogService>()
    private lateinit var viewFactory: ViewFactory

    @BeforeEach
    fun setUp() {
        viewFactory = DefaultViewFactory(viewContext, textProvider, textFormatter, playerTextDrawService, dialogService)
        modelViewerView = ModelViewerView(player, viewContext, viewFactory)
    }

    @Nested
    inner class ModelIdTests {

        @Test
        fun shouldSetModelId() {
            modelViewerView.modelId = 1337

            assertThat(modelViewerView.modelId)
                    .isEqualTo(1337)
        }

        @Test
        fun shouldSupplyModelId() {
            modelViewerView.modelId { 1337 }

            assertThat(modelViewerView.modelId)
                    .isEqualTo(1337)
        }

    }

    @Nested
    inner class VehicleColorsTests {

        @Test
        fun shouldSetVehicleColors() {
            modelViewerView.vehicleColors = vehicleColorsOf(178, 69)

            assertThat(modelViewerView.vehicleColors)
                    .isEqualTo(vehicleColorsOf(178, 69))
        }

        @Test
        fun shouldSupplyVehicleColors() {
            modelViewerView.vehicleColors { vehicleColorsOf(178, 69) }

            assertThat(modelViewerView.vehicleColors)
                    .isEqualTo(vehicleColorsOf(178, 69))
        }

    }

}