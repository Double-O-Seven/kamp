package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionContainer
import ch.leadrian.samp.kamp.view.base.View
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ViewNavigatorTest {

    private lateinit var viewNavigator: ViewNavigator
    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val player = mockk<Player>()
    private val viewNavigation = mockk<ViewNavigation>()

    @BeforeEach
    fun setUp() {
        viewNavigator = ViewNavigator(callbackListenerManager)
        val extensions = mockk<EntityExtensionContainer<Player>> {
            every { get(ViewNavigation::class) } returns viewNavigation
        }
        every { player.extensions } returns extensions
    }

    @Test
    fun shouldRegisterAsCallbackListener() {
        every { callbackListenerManager.register(any()) } just Runs

        viewNavigator.initialize()

        verify { callbackListenerManager.register(viewNavigator) }
    }

    @Test
    fun givenTextDrawIsNotNullItShouldReturnNotFound() {
        val textDraw = mockk<TextDraw>()

        val result = viewNavigator.onPlayerClickTextDraw(player, textDraw)

        assertThat(result)
                .isEqualTo(OnPlayerClickTextDrawListener.Result.NotFound)
    }

    @Test
    fun givenMouseIsNotUsedItShouldReturnNotFound() {
        every { viewNavigation.isMouseUsed } returns false

        val result = viewNavigator.onPlayerClickTextDraw(player, null)

        assertThat(result)
                .isEqualTo(OnPlayerClickTextDrawListener.Result.NotFound)
    }

    @Test
    fun givenMouseIsUsedAndManualNavigationIsAllowedItShouldPopNavigation() {
        viewNavigation.apply {
            every { isMouseUsed } returns true
            every { isManualNavigationAllowed } returns true
            every { pop() } just Runs
        }

        val result = viewNavigator.onPlayerClickTextDraw(player, null)

        verify { viewNavigation.pop() }
        assertThat(result)
                .isEqualTo(OnPlayerClickTextDrawListener.Result.Processed)
    }

    @Test
    fun givenMouseIsUsedAndManualNavigationIsAllowedItShouldSelectTextDrawOnPlayer() {
        val view = mockk<View> {
            every { hoverColor } returns Colors.PINK
        }
        viewNavigation.apply {
            every { isMouseUsed } returns true
            every { isManualNavigationAllowed } returns false
            every { top } returns view
        }
        every { player.selectTextDraw(any()) } just Runs

        val result = viewNavigator.onPlayerClickTextDraw(player, null)

        verify { player.selectTextDraw(Colors.PINK) }
        assertThat(result)
                .isEqualTo(OnPlayerClickTextDrawListener.Result.Processed)
    }

    @Test
    fun givenMouseIsUsedAndManualNavigationAndTopIsNullItShouldDoNothing() {
        viewNavigation.apply {
            every { isMouseUsed } returns true
            every { isManualNavigationAllowed } returns false
            every { top } returns null
        }

        val result = viewNavigator.onPlayerClickTextDraw(player, null)

        assertThat(result)
                .isEqualTo(OnPlayerClickTextDrawListener.Result.Processed)
    }

}