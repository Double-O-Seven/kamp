package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.Player
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class OnPlayerExitedMenuTests {

        @Test
        fun givenPlayerHasMenuItShouldReturnExecuteOnExit() {
            val menu = mockk<Menu> {
                every { onExit(any<Player>()) } just Runs
            }
            every { player.menu } returns menu

            menuCallbackListener.onPlayerExitedMenu(player)

            verify(exactly = 1) { menu.onExit(player) }
        }

        @Test
        fun givenPlayerHasNoMenuItShouldNotDoAnything() {
            every { player.menu } returns null

            val caughtThrowable = catchThrowable { menuCallbackListener.onPlayerExitedMenu(player) }

            assertThat(caughtThrowable)
                    .isNull()
        }

    }

    @Nested
    inner class OnPlayerSelectedMenuRowTests {

        @Test
        fun givenPlayerHasMenuItShouldReturnExecuteOnExit() {
            val menu = mockk<Menu> {
                every { onSelectedMenuRow(any(), any()) } just Runs
            }
            every { player.menu } returns menu

            menuCallbackListener.onPlayerSelectedMenuRow(player, 13)

            verify(exactly = 1) { menu.onSelectedMenuRow(player, 13) }
        }

        @Test
        fun givenPlayerHasNoMenuItShouldNotDoAnything() {
            every { player.menu } returns null

            val caughtThrowable = catchThrowable {
                menuCallbackListener.onPlayerSelectedMenuRow(player, 13)
            }

            assertThat(caughtThrowable)
                    .isNull()
        }

    }

}