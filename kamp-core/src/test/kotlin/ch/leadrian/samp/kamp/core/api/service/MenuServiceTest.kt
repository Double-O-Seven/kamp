package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.MenuFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

internal class MenuServiceTest {

    private lateinit var menuService: MenuService

    private val menuFactory = mockk<MenuFactory>()
    private val menuRegistry = mockk<MenuRegistry>()
    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
    private val textProvider = mockk<TextProvider>()

    @BeforeEach
    fun setUp() {
        menuService = MenuService(menuFactory, menuRegistry, nativeFunctionExecutor, textProvider)
    }

    @Nested
    inner class CreateSingleColumnMenuTests {

        private val menu = mockk<Menu>()

        @Test
        fun shouldCreateMenu() {
            every {
                menuFactory.create(vector2DOf(1f, 2f), 3f, "Hi there", Locale.GERMANY)
            } returns menu

            val createdMenu = menuService.createSingleColumnMenu(
                    position = vector2DOf(1f, 2f),
                    columnWidth = 3f,
                    title = "Hi there",
                    locale = Locale.GERMANY
            )

            assertThat(createdMenu)
                    .isEqualTo(menu)
        }

        @Test
        fun givenTitleTextKeyItShouldCreateMenu() {
            val textKey = TextKey("menu.title")
            val locale = Locale.GERMANY
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            every {
                menuFactory.create(vector2DOf(1f, 2f), 3f, "Hi there", locale)
            } returns menu

            val createdMenu = menuService.createSingleColumnMenu(
                    position = vector2DOf(1f, 2f),
                    columnWidth = 3f,
                    titleTextKey = textKey,
                    locale = locale
            )

            assertThat(createdMenu)
                    .isEqualTo(menu)
        }

    }

    @Nested
    inner class CreateDoubleColumnMenuTests {

        private val menu = mockk<Menu>()

        @Test
        fun shouldCreateMenu() {
            every {
                menuFactory.create(vector2DOf(1f, 2f), 3f, 4f, "Hi there", Locale.GERMANY)
            } returns menu

            val createdMenu = menuService.createDoubleColumnMenu(
                    position = vector2DOf(1f, 2f),
                    columnWidth1 = 3f,
                    columnWidth2 = 4f,
                    title = "Hi there",
                    locale = Locale.GERMANY
            )

            assertThat(createdMenu)
                    .isEqualTo(menu)
        }

        @Test
        fun givenTitleTextKeyItShouldCreateMenu() {
            val textKey = TextKey("menu.title")
            val locale = Locale.GERMANY
            every { textProvider.getText(locale, textKey) } returns "Hi there"
            every {
                menuFactory.create(vector2DOf(1f, 2f), 3f, 4f, "Hi there", locale)
            } returns menu

            val createdMenu = menuService.createDoubleColumnMenu(
                    position = vector2DOf(1f, 2f),
                    columnWidth1 = 3f,
                    columnWidth2 = 4f,
                    titleTextKey = textKey,
                    locale = locale
            )

            assertThat(createdMenu)
                    .isEqualTo(menu)
        }

    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun isValidShouldReturnExpectedResult(expectedResult: Boolean) {
        val menuId = MenuId.valueOf(1337)
        every { nativeFunctionExecutor.isValidMenu(menuId.value) } returns expectedResult

        val result = menuService.isValid(menuId)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    @Nested
    inner class GetMenuTests {

        @Test
        fun givenMenuIdIsValidItShouldReturnMenu() {
            val menuId = MenuId.valueOf(1337)
            val expectedMenu = mockk<Menu>()
            every { menuRegistry[menuId] } returns expectedMenu

            val menu = menuService.getMenu(menuId)

            assertThat(menu)
                    .isEqualTo(expectedMenu)
        }

        @Test
        fun givenInvalidMenuIdItShouldThrowException() {
            val menuId = MenuId.valueOf(1337)
            every { menuRegistry[menuId] } returns null

            val caughtThrowable = catchThrowable { menuService.getMenu(menuId) }

            assertThat(caughtThrowable)
                    .isInstanceOf(NoSuchEntityException::class.java)
                    .hasMessage("No menu with ID 1337")
        }

    }

    @Test
    fun shouldReturnAllMenus() {
        val menu1 = mockk<Menu>()
        val menu2 = mockk<Menu>()
        every { menuRegistry.getAll() } returns listOf(menu1, menu2)

        val menus = menuService.getAllMenus()

        assertThat(menus)
                .containsExactly(menu1, menu2)
    }

}