package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.stubDefaultViewFactory
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

internal class VerticalScrollBarViewTest : ScrollBarViewTest<VerticalScrollBarView> {

    private val player = mockk<Player>()
    private val viewContext = mockk<ViewContext>()
    private lateinit var viewFactory: ViewFactory

    @BeforeEach
    fun setUp() {
        viewFactory = stubDefaultViewFactory(viewContext = viewContext)
    }

    override fun getScrollBarView(): VerticalScrollBarView =
            VerticalScrollBarView(player, viewContext, viewFactory, mockk())

}