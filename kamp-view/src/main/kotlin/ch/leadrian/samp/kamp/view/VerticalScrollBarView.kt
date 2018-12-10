package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.constants.TextDrawCodes
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels

open class VerticalScrollBarView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory
) : ScrollBarView(player, viewContext) {

    private lateinit var backgroundView: BackgroundView

    private lateinit var scrollUpButtonView: ButtonView

    private lateinit var scrollDownButtonView: ButtonView

    private lateinit var scrollingBarView: BackgroundView

    override var color: Color = Colors.GREY
        set(value) {
            field = value.toColor()
            scrollUpButtonView.backgroundColor = field
            scrollDownButtonView.backgroundColor = field
            scrollingBarView.color = field
        }

    override var backgroundColor: Color = Colors.LIGHT_GRAY
        set(value) {
            field = value.toColor()
            backgroundView.color = field
        }

    init {
        with(viewFactory) {
            backgroundView = this@VerticalScrollBarView.backgroundView {
                color = this@VerticalScrollBarView.backgroundColor
                scrollUpButtonView = buttonView {
                    top = 0.pixels()
                    backgroundColor = this@VerticalScrollBarView.color
                    height = pixels { parentArea.width }
                    text = TextDrawCodes.UP
                    textPadding = 5.percent()
                    onClick {
                        scroll(-1)
                    }
                }
                scrollDownButtonView = buttonView {
                    bottom = 0.pixels()
                    backgroundColor = this@VerticalScrollBarView.color
                    height = pixels { parentArea.width }
                    text = TextDrawCodes.DOWN
                    textPadding = 5.percent()
                    onClick {
                        scroll(+1)
                    }
                }
                view {
                    topToBottomOf(scrollUpButtonView)
                    bottomToTopOf(scrollDownButtonView)
                    scrollingBarView = backgroundView {
                        color = this@VerticalScrollBarView.color
                        top = percent {
                            val numberOfTicks = scrollBarAdapter.numberOfTicks
                            when (numberOfTicks) {
                                0 -> 0f
                                else -> 100f * (currentPosition.toFloat() / numberOfTicks.toFloat())
                            }
                        }
                        height = percent {
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