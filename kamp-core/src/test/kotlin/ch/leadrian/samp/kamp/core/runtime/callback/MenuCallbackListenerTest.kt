package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MenuCallbackListenerTest {

    private lateinit var menuCallbackListener: MenuCallbackListener

    private val callbackListenerManager = mockk<CallbackListenerManager>()
    private val player = mockk<Player>()

    @BeforeEach
    fun setUp() {
        menuCallbackListener = MenuCallbackListener(callbackListenerManager)
    }

    @Test
    fun shouldRegisterOnInitialize() {
        every { callbackListenerManager.register(any()) } just Runs

        menuCallbackListener.initialize()

        verify { callbackListenerManager.register(menuCallbackListener) }
    }

    @Test
    fun onPlayerExitedMenuShouldCallMenuOnExit() {
        val menu = mockk<Menu> {
            every { onExit(any<Player>()) } just Runs
        }

        menuCallbackListener.onPlayerExitedMenu(player, menu)

        verify(exactly = 1) { menu.onExit(player) }
    }

    @Test
    fun shouldCallOnSelectedOnMenuRow() {
        val menuRow = mockk<MenuRow> {
            every { onSelected(any<Player>()) } just Runs
        }

        menuCallbackListener.onPlayerSelectedMenuRow(player, menuRow)

        verify(exactly = 1) { menuRow.onSelected(player) }
    }

}