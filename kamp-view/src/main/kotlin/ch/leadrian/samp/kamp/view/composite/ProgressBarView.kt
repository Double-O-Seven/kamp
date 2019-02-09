package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.ClickableView
import ch.leadrian.samp.kamp.view.base.SpriteView
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import ch.leadrian.samp.kamp.view.style.ProgressBarStyle
import ch.leadrian.samp.kamp.view.style.Style

open class ProgressBarView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory
) : ClickableView(player, viewContext) {

    private val outlineSpriteView: SpriteView

    private lateinit var secondarySpriteView: SpriteView

    private lateinit var primarySpriteView: SpriteView

    var direction: Direction = Direction.LEFT_TO_RIGHT

    var outlineSize: ViewDimension = 2.pixels()

    var outlineColor: Color
        get() = outlineSpriteView.color
        set(value) {
            outlineSpriteView.color = value
        }

    fun outlineColor(outlineColorSupplier: () -> Color) {
        outlineSpriteView.color(outlineColorSupplier)
    }

    var primaryColor: Color
        get() = primarySpriteView.color
        set(value) {
            primarySpriteView.color = value
        }

    fun primaryColor(primaryColorSupplier: () -> Color) {
        primarySpriteView.color(primaryColorSupplier)
    }

    var secondaryColor: Color
        get() = secondarySpriteView.color
        set(value) {
            secondarySpriteView.color = value
        }

    fun secondaryColor(secondaryColorSupplier: () -> Color) {
        secondarySpriteView.color(secondaryColorSupplier)
    }

    private val maxValueSupplier: ValueSupplier<Int> = ValueSupplier(100)

    var maxValue: Int by maxValueSupplier

    fun maxValue(maxValueSupplier: () -> Int) {
        this.maxValueSupplier.value(maxValueSupplier)
    }

    private val valueSupplier: ValueSupplier<Int> = ValueSupplier(100)

    var value: Int by valueSupplier

    fun value(valueSupplier: () -> Int) {
        this.valueSupplier.value(valueSupplier)
    }

    init {
        with(viewFactory) {
            outlineSpriteView = spriteView {
                spriteName = "LD_SPAC:white"
                color = Colors.BLACK
                secondarySpriteView = spriteView {
                    spriteName = "LD_SPAC:white"
                    color = Colors.DARK_RED
                    primarySpriteView = spriteView {
                        spriteName = "LD_SPAC:white"
                        color = Colors.RED
                    }
                }
            }
        }
    }

    private val progress: Float
        get() {
            val maxValue = this.maxValue
            return when {
                maxValue <= 0 -> 0f
                else -> value.coerceIn(0, maxValue).toFloat() * 100f / maxValue.toFloat()
            }
        }

    override fun onDraw() {
        super.onDraw()
        outlineSpriteView.setPadding(outlineSize)
        updateProgress()
    }

    override fun applyStyle(style: Style): Boolean {
        if (style is ProgressBarStyle) {
            primaryColor = style.progressBarPrimaryColor
            secondaryColor = style.progressBarSecondaryColor
            outlineColor = style.progressBarOutlineColor
        }
        return super.applyStyle(style)
    }

    private fun updateProgress() {
        when (direction) {
            Direction.LEFT_TO_RIGHT -> {
                primarySpriteView.apply {
                    leftToLeftOf(secondarySpriteView)
                    width = progress.percent()
                    right = null
                }
            }
            Direction.RIGHT_TO_LEFT -> {
                primarySpriteView.apply {
                    rightToRightOf(secondarySpriteView)
                    width = progress.percent()
                    left = null
                }
            }
            Direction.TOP_TO_BOTTOM -> {
                primarySpriteView.apply {
                    topToTopOf(secondarySpriteView)
                    height = progress.percent()
                    bottom = null
                }
            }
            Direction.BOTTOM_TO_TOP -> {
                primarySpriteView.apply {
                    bottomToBottomOf(secondarySpriteView)
                    height = progress.percent()
                    top = null
                }
            }
        }
    }

    enum class Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP
    }
}