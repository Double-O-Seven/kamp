package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.MutableVector3D
import ch.leadrian.samp.kamp.core.api.data.VehicleColors
import ch.leadrian.samp.kamp.core.api.data.mutableVector3DOf
import ch.leadrian.samp.kamp.core.api.data.vehicleColorsOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.ModelView
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels

open class ModelViewerView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory
) : View(player, viewContext) {

    private lateinit var modelView: ModelView

    var zoomStep: Float = 0.05f

    var rotationStep: Float = 15f

    var modelId: Int
        get() = modelView.modelId
        set(value) {
            modelView.modelId = value
        }

    fun modelId(modelIdSupplier: () -> Int) {
        modelView.modelId(modelIdSupplier)
    }

    var rotation: MutableVector3D = mutableVector3DOf(-16.0f, 0.0f, -55.0f)

    var zoom: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 5f)
        }

    var vehicleColors: VehicleColors?
        get() = modelView.vehicleColors
        set(value) {
            modelView.vehicleColors = value
        }

    fun vehicleColors(vehicleColorsSupplier: () -> VehicleColors?) {
        modelView.vehicleColors(vehicleColorsSupplier)
    }

    var buttonSize: ViewDimension = 16.pixels()

    init {
        val buttonSizeDelegate = pixels { parentValue -> buttonSize.getValue(parentValue) }
        with(viewFactory) {
            val zoomTextView = textView {
                height = buttonSizeDelegate
                val widthInPixels = pixels { parentValue -> buttonSize.getValue(parentValue) * 4f }
                width = widthInPixels
                bottom = 0.pixels()
                val horizontalOffset = pixels { parentValue -> (parentValue - widthInPixels.getValue(parentValue)) / 2f }
                left = horizontalOffset
                right = horizontalOffset
                alignment = TextDrawAlignment.CENTERED
                letterHeight = 100.percent()
                text {
                    val percent = Math.round(zoom * 100f)
                    "$percent%"
                }
            }
            buttonView {
                height = buttonSizeDelegate
                width = buttonSizeDelegate
                bottom = 0.pixels()
                rightToLeftOf(zoomTextView)
                alignment = TextDrawAlignment.CENTERED
                text = "-"
                font = TextDrawFont.BANK_GOTHIC
                onClick {
                    zoom -= zoomStep
                    this@ModelViewerView.draw()
                }
            }
            buttonView {
                height = buttonSizeDelegate
                width = buttonSizeDelegate
                bottom = 0.pixels()
                leftToRightOf(zoomTextView)
                alignment = TextDrawAlignment.CENTERED
                text = "+"
                font = TextDrawFont.BANK_GOTHIC
                onClick {
                    zoom += zoomStep
                    this@ModelViewerView.draw()
                }
            }
            view {
                top = 0.pixels()
                bottomToTopOf(zoomTextView)
                modelView = modelView {
                    setMargin(buttonSizeDelegate)
                    rotation { this@ModelViewerView.rotation }
                    zoom { this@ModelViewerView.zoom }
                    vehicleColors = vehicleColorsOf(3, 1)
                }
                spriteView {
                    left = 0.pixels()
                    width = buttonSizeDelegate
                    top = pixels { parentValue -> (parentValue - buttonSize.getValue(parentValue)) / 2f }
                    height = buttonSizeDelegate
                    spriteName = "ld_beat:left"
                    enable()
                    onClick {
                        rotation.z -= rotationStep
                        this@ModelViewerView.draw()
                    }
                }
                spriteView {
                    right = 0.pixels()
                    width = buttonSizeDelegate
                    top = pixels { parentValue -> (parentValue - buttonSize.getValue(parentValue)) / 2f }
                    height = buttonSizeDelegate
                    spriteName = "ld_beat:right"
                    enable()
                    onClick {
                        rotation.z += rotationStep
                        this@ModelViewerView.draw()
                    }
                }
                spriteView {
                    left = 0.pixels()
                    width = buttonSizeDelegate
                    top = pixels { parentValue -> (parentValue - buttonSize.getValue(parentValue)) / 2f }
                    height = buttonSizeDelegate
                    spriteName = "ld_beat:left"
                    enable()
                    onClick {
                        rotation.z -= rotationStep
                        this@ModelViewerView.draw()
                    }
                }
                spriteView {
                    top = 0.pixels()
                    width = buttonSizeDelegate
                    left = pixels { parentValue -> (parentValue - buttonSize.getValue(parentValue)) / 2f }
                    height = buttonSizeDelegate
                    spriteName = "ld_beat:up"
                    enable()
                    onClick {
                        rotation.x += rotationStep
                        this@ModelViewerView.draw()
                    }
                }
                spriteView {
                    bottom = 0.pixels()
                    width = buttonSizeDelegate
                    left = pixels { parentValue -> (parentValue - buttonSize.getValue(parentValue)) / 2f }
                    height = buttonSizeDelegate
                    spriteName = "ld_beat:down"
                    enable()
                    onClick {
                        rotation.x -= rotationStep
                        this@ModelViewerView.draw()
                    }
                }
            }
        }

    }

}