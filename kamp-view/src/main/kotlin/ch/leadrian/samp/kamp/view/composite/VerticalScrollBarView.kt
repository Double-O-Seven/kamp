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

open class VerticalScrollBarView(
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
                color { this@VerticalScrollBarView.backgroundColor }
                val scrollUpView = backgroundView {
                    top = 0.pixels()
                    height = pixels { parentArea.width }
                    color { this@VerticalScrollBarView.color }
                    spriteView {
                        setMargin(5.percent())
                        spriteName = "ld_beat:up"
                        color { buttonColor }
                        enable()
                        onClick { scroll(-1) }
                    }
                }
                val scrollDownView = backgroundView {
                    bottom = 0.pixels()
                    height = pixels { parentArea.width }
                    color { this@VerticalScrollBarView.color }
                    spriteView {
                        setMargin(5.percent())
                        spriteName = "ld_beat:down"
                        color { buttonColor }
                        enable()
                        onClick { scroll(+1) }
                    }
                }
                view {
                    topToBottomOf(scrollUpView)
                    bottomToTopOf(scrollDownView)
                    backgroundView {
                        top = percent {
                            val numberOfTicks = adapter.numberOfTicks
                            when (numberOfTicks) {
                                0 -> 0f
                                else -> 100f * (currentPosition.toFloat() / numberOfTicks.toFloat())
                            }
                        }
                        height = percent {
                            val windowSize = adapter.windowSize
                            100f * (windowSize.toFloat() / Math.max(windowSize, adapter.numberOfTicks).toFloat())
                        }
                        color { this@VerticalScrollBarView.color }
                    }
                }
            }
        }
    }

}