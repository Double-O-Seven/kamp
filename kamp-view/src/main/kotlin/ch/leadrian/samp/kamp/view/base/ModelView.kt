package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext

open class ModelView(
        player: Player,
        viewContext: ViewContext,
        private val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private val colorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.WHITE)

    var color: Color by colorSupplier

    fun color(colorSupplier: () -> Color) {
        this.colorSupplier.value(colorSupplier)
    }

    private val modelIdSupplier: ValueSupplier<Int> = ValueSupplier(18750)

    var modelId: Int by modelIdSupplier

    fun modelId(modelIdSupplier: () -> Int) {
        this.modelIdSupplier.value(modelIdSupplier)
    }

    private val rotationSupplier: ValueSupplier<Vector3D> = ValueSupplier(vector3DOf(-16.0f, 0.0f, -55.0f))

    var rotation: Vector3D by rotationSupplier

    fun rotation(rotationSupplier: () -> Vector3D) {
        this.rotationSupplier.value(rotationSupplier)
    }

    private val zoomSupplier: ValueSupplier<Float> = ValueSupplier(1f)

    var zoom: Float by zoomSupplier

    fun zoom(zoomSupplier: () -> Float) {
        this.zoomSupplier.value(zoomSupplier)
    }

    private val vehicleColorsSupplier: ValueSupplier<VehicleColors?> = ValueSupplier(null as VehicleColors?)

    var vehicleColors: VehicleColors? by vehicleColorsSupplier

    fun vehicleColors(vehicleColorsSupplier: () -> VehicleColors?) {
        this.vehicleColorsSupplier.value(vehicleColorsSupplier)
    }

    private fun Rectangle.transformToCenteredSquare(): Rectangle {
        return if (width > height) {
            val d = (width - height) / 2f
            toMutableRectangle().apply {
                minX += d
                maxX -= d
            }
        } else {
            val d = (height - width) / 2f
            toMutableRectangle().apply {
                minY += d
                maxY -= d
            }
        }
    }

    override fun onDraw() {
        if (isInvalidated) {
            replaceTextDraw(paddingArea.transformToCenteredSquare())
        } else {
            updateTextDraw()
        }
    }

    private fun replaceTextDraw(area: Rectangle) {
        textDraw?.destroy()

        textDraw = playerTextDrawService.createPlayerTextDraw(
                player,
                TextDrawCodes.EMPTY_TEXT,
                vector2DOf(
                        x = area.minX,
                        y = screenMinYToTextDrawMinY(area.minY, offset = 0f)
                )
        ).also {
            it.alignment = TextDrawAlignment.LEFT
            it.textSize = vector2DOf(
                    x = area.width,
                    y = screenHeightToTextDrawHeight(area.height, offset = 0f)
            )
            it.font = TextDrawFont.MODEL_PREVIEW
            it.isSelectable = isEnabled
            it.previewModelId = modelId
            it.backgroundColor = Colors.TRANSPARENT
            it.color = color
            it.previewModelVehicleColors = vehicleColors
            it.setPreviewModelRotation(rotation, zoom)
            it.onClick { click() }
            it.show()
        }
    }

    private fun updateTextDraw() {
        textDraw?.also {
            var show = false

            if (it.previewModelId != modelId) {
                it.previewModelId = modelId
                show = true
            }

            if (it.previewModelVehicleColors != vehicleColors) {
                it.previewModelVehicleColors = vehicleColors
                show = true
            }

            if (it.isSelectable != isEnabled) {
                it.isSelectable = isEnabled
                show = true
            }

            if (it.previewModelRotation != rotation || it.previewModelZoom != zoom) {
                it.setPreviewModelRotation(rotation, zoom)
                show = true
            }

            if (it.color != color) {
                it.color = color
                show = true
            }

            if (show) {
                it.show()
            }
        }
    }

    override fun onHide() {
        destroyTextDraw()
    }

    override fun onDestroy() {
        destroyTextDraw()
    }

    private fun destroyTextDraw() {
        textDraw?.destroy()
        textDraw = null
    }
}