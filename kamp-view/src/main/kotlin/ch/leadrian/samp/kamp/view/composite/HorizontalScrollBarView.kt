package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.BackgroundView
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels

open class HorizontalScrollBarView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory
) : ScrollBarView(player, viewContext) {

    private lateinit var backgroundView: BackgroundView

    private lateinit var scrollLeftButtonView: ButtonView

    private lateinit var scrollRightButtonView: ButtonView

    private lateinit var scrollingBarView: BackgroundView

    override var color: Color = Colors.GREY
        set(value) {
            field = value.toColor()
            scrollLeftButtonView.backgroundColor = field
            scrollRightButtonView.backgroundColor = field
            scrollingBarView.color = field
        }

    override var backgroundColor: Color = Colors.LIGHT_GRAY
        set(value) {
            field = value.toColor()
            backgroundView.color = field
        }

    init {
        with(viewFactory) {
            backgroundView = this@HorizontalScrollBarView.backgroundView {
                color = this@HorizontalScrollBarView.backgroundColor
                scrollLeftButtonView = buttonView {
                    left = 0.pixels()
                    backgroundColor = this@HorizontalScrollBarView.color
                    width = pixels { parentArea.height }
                    text = TextDrawCodes.LEFT
                    textPadding = 5.percent()
                    onClick {
                        scroll(-1)
                    }
                }
                scrollRightButtonView = buttonView {
                    right = 0.pixels()
                    backgroundColor = this@HorizontalScrollBarView.color
                    width = pixels { parentArea.height }
                    text = TextDrawCodes.RIGHT
                    textPadding = 5.percent()
                    onClick {
                        scroll(+1)
                    }
                }
                view {
                    leftToRightOf(scrollLeftButtonView)
                    rightToLeftOf(scrollRightButtonView)
                    scrollingBarView = backgroundView {
                        color = this@HorizontalScrollBarView.color
                        left = percent {
                            val numberOfTicks = scrollBarAdapter.numberOfTicks
                            when (numberOfTicks) {
                                0 -> 0f
                                else -> 100f * (currentPosition.toFloat() / numberOfTicks.toFloat())
                            }
                        }
                        width = percent {
                            val windowSize = scrollBarAdapter.windowSize
                            100f * (windowSize.toFloat() / Math.max(windowSize, scrollBarAdapter.numberOfTicks).toFloat())
                        }
                    }
                }
            }
        }
    }

    override fun onScroll() {
        invalidate()
        draw()
    }

}