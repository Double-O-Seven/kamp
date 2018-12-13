package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.base.View
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ViewNavigationElementTest {

    @Nested
    inner class NavigateToTests {

        @Test
        fun givenMouseIsUsedItShouldShowViewWithTextDrawSelectionEnabled() {
            val player = mockk<Player> {
                every { selectTextDraw(any()) } just Runs
            }
            val view = mockk<View> {
                every { show(any(), any()) } just Runs
                every { draw() } just Runs
                every { this@mockk.player } returns player
                every { hoverColor } returns Colors.RED
            }
            val viewNavigationElement = ViewNavigationElement(
                    view = view,
                    allowManualNavigation = false,
                    useMouse = true
            )

            viewNavigationElement.navigateTo()

            verifyOrder {
                view.show(draw = false, invalidate = true)
                player.selectTextDraw(Colors.RED)
                view.draw()
            }
        }

        @Test
        fun givenMouseIsNotUsedItShouldShowViewWithTextDrawSelectionDisabled() {
            val player = mockk<Player> {
                every { cancelSelectTextDraw() } just Runs
            }
            val view = mockk<View> {
                every { show(any(), any()) } just Runs
                every { draw() } just Runs
                every { this@mockk.player } returns player
            }
            val viewNavigationElement = ViewNavigationElement(
                    view = view,
                    allowManualNavigation = false,
                    useMouse = false
            )

            viewNavigationElement.navigateTo()

            verifyOrder {
                view.show(draw = false, invalidate = true)
                player.cancelSelectTextDraw()
                view.draw()
            }
        }
    }

}