package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class MenuFactoryTest {

    private lateinit var menuFactory: MenuFactory

    private val menuRegistry = mockk<MenuRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val textProvider = mockk<TextProvider>()
    private val textFormatter = mockk<TextFormatter>()

    @BeforeEach
    fun setUp() {
        every { nativeFunctionExecutor.createMenu(any(), any(), any(), any(), any(), any()) } returns 0
        every { menuRegistry.register(any()) } just Runs
        every { menuRegistry.unregister(any()) } just Runs
        menuFactory = MenuFactory(menuRegistry, nativeFunctionExecutor, textProvider, textFormatter)
    }

    @Nested
    inner class OneColumnTests {

        @Test
        fun shouldCreateMenu() {
            menuFactory.create(
                    title = "Test",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth = 3f
            )

            verify {
                nativeFunctionExecutor.createMenu(
                        title = "Test",
                        x = 1f,
                        y = 2f,
                        col1width = 3f,
                        col2width = 0f,
                        columns = 1
                )
            }
        }

        @Test
        fun shouldRegisterMenu() {
            val menu = menuFactory.create(
                    title = "Test",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth = 3f
            )

            verify { menuRegistry.register(menu) }
        }

        @Test
        fun shouldUnregisterMenuOnDestroy() {
            every { nativeFunctionExecutor.destroyMenu(any()) } returns true
            val menu = menuFactory.create(
                    title = "Test",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth = 3f
            )
            val onDestroy = mockk<Menu.() -> Unit>(relaxed = true)
            menu.onDestroy(onDestroy)

            menu.destroy()

            verify { onDestroy.invoke(menu) }
        }

    }

    @Nested
    inner class TwoColumnTests {

        @Test
        fun shouldCreateMenu() {
            menuFactory.create(
                    title = "Test",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth1 = 3f,
                    columnWidth2 = 4f
            )

            verify {
                nativeFunctionExecutor.createMenu(
                        title = "Test",
                        x = 1f,
                        y = 2f,
                        col1width = 3f,
                        col2width = 4f,
                        columns = 2
                )
            }
        }

        @Test
        fun shouldRegisterMenu() {
            val menu = menuFactory.create(
                    title = "Test",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth1 = 3f,
                    columnWidth2 = 4f
            )

            verify { menuRegistry.register(menu) }
        }

        @Test
        fun shouldUnregisterMenuOnDestroy() {
            every { nativeFunctionExecutor.destroyMenu(any()) } returns true
            val menu = menuFactory.create(
                    title = "Test",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth1 = 3f,
                    columnWidth2 = 4f
            )
            val onDestroy = mockk<Menu.() -> Unit>(relaxed = true)
            menu.onDestroy(onDestroy)

            menu.destroy()

            verify { onDestroy.invoke(menu) }
        }

    }

}