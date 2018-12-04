package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.View
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ViewNavigationElementTest {

    @Nested
    inner class NavigateToTests {

        @Test
        fun shouldShowView() {
            val view = mockk<View> {
                every { show(any(), any()) } just Runs
                every { draw() } just Runs
            }
            val viewNavigationElement = ViewNavigationElement(
                    view = view,
                    isManualNavigationAllowed = false,
                    useMouse = false,
                    destroyOnPop = true,
                    hoverColor = Colors.RED
            )

            viewNavigationElement.navigateTo()

            verifyOrder {
                view.show(draw = false, invalidate = true)
                view.draw()
            }
        }

        @Test
        fun givenMouseIsUsedItShouldEnableTextDrawSelection() {
            val player = mockk<Player> {
                every { selectTextDraw(any()) } just Runs
            }
            val view = mockk<View> {
                every { show(any(), any()) } just Runs
                every { draw() } just Runs
                every { this@mockk.player } returns player
            }
            val viewNavigationElement = ViewNavigationElement(
                    view = view,
                    isManualNavigationAllowed = false,
                    useMouse = true,
                    destroyOnPop = true,
                    hoverColor = Colors.RED
            )

            viewNavigationElement.navigateTo()

            verify { player.selectTextDraw(Colors.RED) }
        }
    }

    @Nested
    inner class OnPopTests {

        @Test
        fun givenDestroyOnPopIsTrueItShouldDestroyView() {
            val view = mockk<View> {
                every { destroy() } just Runs
            }
            val viewNavigationElement = ViewNavigationElement(
                    view = view,
                    isManualNavigationAllowed = false,
                    useMouse = false,
                    destroyOnPop = true,
                    hoverColor = Colors.RED
            )

            viewNavigationElement.onPop()

            verify { view.destroy() }
        }

        @Test
        fun givenDestroyOnPopIsFalseItShouldHideView() {
            val view = mockk<View> {
                every { hide() } just Runs
            }
            val viewNavigationElement = ViewNavigationElement(
                    view = view,
                    isManualNavigationAllowed = false,
                    useMouse = false,
                    destroyOnPop = false,
                    hoverColor = Colors.RED
            )

            viewNavigationElement.onPop()

            verify { view.hide() }
        }
    }

}