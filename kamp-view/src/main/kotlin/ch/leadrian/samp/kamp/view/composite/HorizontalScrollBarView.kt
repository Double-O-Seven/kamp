package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels

open class HorizontalScrollBarView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory,
        adapter: ScrollBarAdapter
) : ScrollBarView(player, viewContext, adapter) {

    private val colorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.GREY)

    override var color: Color by colorSupplier

    override fun color(colorSupplier: () -> Color) {
        this.colorSupplier.value(colorSupplier)
    }

    private val backgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.LIGHT_GRAY)

    override var backgroundColor: Color by backgroundColorSupplier

    override fun backgroundColor(backgroundColorSupplier: () -> Color) {
        this.backgroundColorSupplier.value(backgroundColorSupplier)
    }

    private val buttonColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.BLACK)

    override var buttonColor: Color by buttonColorSupplier

    override fun buttonColor(buttonColorSupplier: () -> Color) {
        this.buttonColorSupplier.value(buttonColorSupplier)
    }

    init {
        with(viewFactory) {
            backgroundView {
                color { this@HorizontalScrollBarView.backgroundColor }
                val scrollLeftView = backgroundView {
                    left = 0.pixels()
                    width = pixels { parentArea.height }
                    color { this@HorizontalScrollBarView.color }
                    spriteView {
                        setMargin(5.percent())
                        spriteName = "ld_beat:left"
                        color { buttonColor }
                        enable()
                        onClick { scroll(-1) }
                    }
                }
                val scrollRightView = backgroundView {
                    right = 0.pixels()
                    width = pixels { parentArea.height }
                    color { this@HorizontalScrollBarView.color }
                    spriteView {
                        setMargin(5.percent())
                        spriteName = "ld_beat:right"
                        color { buttonColor }
                        enable()
                        onClick { scroll(+1) }
                    }
                }
                view {
                    leftToRightOf(scrollLeftView)
                    rightToLeftOf(scrollRightView)
                    backgroundView {
                        color { this@HorizontalScrollBarView.color }
                        left = percent {
                            val numberOfTicks = adapter.numberOfTicks
                            when (numberOfTicks) {
                                0 -> 0f
                                else -> 100f * (currentPosition.toFloat() / numberOfTicks.toFloat())
                            }
                        }
                        width = percent {
                            val windowSize = adapter.windowSize
                            100f * (windowSize.toFloat() / Math.max(windowSize, adapter.numberOfTicks).toFloat())
                        }
                    }
                }
            }
        }
    }

}