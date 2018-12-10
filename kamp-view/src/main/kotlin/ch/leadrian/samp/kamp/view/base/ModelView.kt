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
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.layout.screenHeightToTextDrawBoxHeight
import ch.leadrian.samp.kamp.view.layout.screenYCoordinateToTextDrawBoxY

open class ModelView(
        player: Player,
        viewContext: ViewContext,
        protected val playerTextDrawService: PlayerTextDrawService
) : ClickableView(player, viewContext) {

    private var textDraw: PlayerTextDraw? = null

    private var colorSupplier: () -> Color = { Colors.WHITE }

    var color: Color
        get() = colorSupplier()
        set(value) {
            val color = value.toColor()
            colorSupplier = { color }
        }

    fun color(colorSupplier: () -> Color) {
        this.colorSupplier = colorSupplier
    }

    private var modelIdSupplier: () -> Int = { 18750 }

    var modelId: Int
        get() = modelIdSupplier()
        set(value) {
            modelIdSupplier = { value }
        }

    fun modelId(modelIdSupplier: () -> Int) {
        this.modelIdSupplier = modelIdSupplier
    }

    private var rotationSupplier: () -> Vector3D = { vector3DOf(-16.0f, 0.0f, -55.0f) }

    var rotation: Vector3D
        get() = rotationSupplier()
        set(value) {
            val immutableVector3D = value.toVector3D()
            rotationSupplier = { immutableVector3D }
        }

    fun rotation(rotationSupplier: () -> Vector3D) {
        this.rotationSupplier = rotationSupplier
    }

    private var zoomSupplier: () -> Float = { 1f }

    var zoom: Float
        get() = zoomSupplier()
        set(value) {
            zoomSupplier = { value }
        }

    fun zoom(zoomSupplier: () -> Float) {
        this.zoomSupplier = zoomSupplier
    }

    private var vehicleColorsSupplier: (() -> VehicleColors?)? = null

    var vehicleColors: VehicleColors?
        get() = vehicleColorsSupplier?.invoke()
        set(value) {
            vehicleColorsSupplier = when {
                value != null -> {
                    { value }
                }
                else -> null
            }

        }

    fun vehicleColors(vehicleColorsSupplier: () -> VehicleColors?) {
        this.vehicleColorsSupplier = vehicleColorsSupplier
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
                maxX -= d
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

        val textDrawBoxHeight = screenHeightToTextDrawBoxHeight(area.height) / 0.135f
        val delta = area.height - textDrawBoxHeight
        textDraw = playerTextDrawService.createPlayerTextDraw(
                player,
                TextDrawCodes.EMPTY_TEXT,
                vector2DOf(x = area.minX + delta / 2f, y = screenYCoordinateToTextDrawBoxY(area.minY))
        ).also {
            it.alignment = TextDrawAlignment.LEFT
            it.textSize = vector2DOf(x = area.width - delta, y = textDrawBoxHeight)
            it.font = TextDrawFont.MODEL_PREVIEW
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