package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.mutableVector2DOf
import ch.leadrian.samp.kamp.core.api.data.vector2DOf
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.AlreadyDestroyedException
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerExitedMenuReceiverDelegate
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.Locale

internal class MenuTest {

    @Nested
    inner class ConstructorTests {

        @Test
        fun shouldConstructMenu() {
            val menuId = MenuId.valueOf(69)
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createMenu(
                            title = "Hi there",
                            x = 1f,
                            y = 2f,
                            col1width = 5f,
                            col2width = 10f,
                            columns = 2
                    )
                } returns menuId.value
            }

            val menu = Menu(
                    numberOfColumns = 2,
                    title = "Hi there",
                    locale = Locale.GERMANY,
                    position = vector2DOf(x = 1f, y = 2f),
                    columnWidth1 = 5f,
                    columnWidth2 = 10f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = mockk(),
                    textProvider = mockk()
            )

            assertThat(menu.id)
                    .isEqualTo(menuId)
        }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0, 3])
        fun givenInvalidNumberOfColumnsItShouldThrowAnException(numberOfColumns: Int) {
            val caughtThrowable = catchThrowable {
                Menu(
                        numberOfColumns = numberOfColumns,
                        title = "Hi there",
                        locale = Locale.GERMANY,
                        position = vector2DOf(x = 1f, y = 2f),
                        columnWidth1 = 5f,
                        columnWidth2 = 10f,
                        nativeFunctionExecutor = mockk(),
                        textFormatter = mockk(),
                        textProvider = mockk()
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun givenCreateMenuReturnsInvalidMenuIdItShouldThrowCreationFailedException() {
            val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor> {
                every {
                    createMenu(
                            title = "Hi there",
                            x = 1f,
                            y = 2f,
                            col1width = 5f,
                            col2width = 10f,
                            columns = 2
                    )
                } returns SAMPConstants.INVALID_MENU
            }

            val caughtThrowable = catchThrowable {
                Menu(
                        numberOfColumns = 2,
                        title = "Hi there",
                        locale = Locale.GERMANY,
                        position = vector2DOf(x = 1f, y = 2f),
                        columnWidth1 = 5f,
                        columnWidth2 = 10f,
                        nativeFunctionExecutor = nativeFunctionExecutor,
                        textFormatter = mockk(),
                        textProvider = mockk()
                )
            }

            assertThat(caughtThrowable)
                    .isInstanceOf(CreationFailedException::class.java)
        }
    }

    @Nested
    inner class PostConstructionTests {

        private val menuId = MenuId.valueOf(69)
        private val locale = Locale.GERMANY
        private lateinit var menu: Menu
        private val onPlayerExitedMenuReceiver = mockk<OnPlayerExitedMenuReceiverDelegate>()

        private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()
        private val textProvider = mockk<TextProvider>()
        private val textFormatter = mockk<TextFormatter>()

        @BeforeEach
        fun setUp() {
            every { nativeFunctionExecutor.createMenu(any(), any(), any(), any(), any(), any()) } returns menuId.value
            menu = Menu(
                    numberOfColumns = 2,
                    title = "Hi there",
                    locale = locale,
                    position = mutableVector2DOf(x = 1f, y = 2f),
                    columnWidth1 = 5f,
                    columnWidth2 = 10f,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textFormatter = textFormatter,
                    textProvider = textProvider,
                    onPlayerExitedMenuReceiver = onPlayerExitedMenuReceiver
            )
        }

        @Test
        fun shouldInitializePosition() {
            val position = menu.position

            assertThat(position)
                    .isEqualTo(vector2DOf(x = 1f, y = 2f))
        }

        @Nested
        inner class AddItemTests {

            @ParameterizedTest
            @ValueSource(ints = [0, 1])
            fun shouldAddMenuItem(column: Int) {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returns 0

                menu.addItem(column, "Hi there")

                verify {
                    nativeFunctionExecutor.addMenuItem(
                            menuid = menuId.value,
                            column = column,
                            menutext = "Hi there"
                    )
                }
            }

            @ParameterizedTest
            @ValueSource(ints = [-1, 2])
            fun givenInvalidColumnItShouldThrowException(column: Int) {
                val caughtThrowable = catchThrowable { menu.addItem(column, "Hi there") }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalArgumentException::class.java)
            }

            @Test
            fun givenFormattedTextItShouldAddMenuItem() {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returns 0
                every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

                menu.addItem(0, "Hi %s", "there")

                verify { nativeFunctionExecutor.addMenuItem(menuid = menuId.value, column = 0, menutext = "Hi there") }
            }

            @Test
            fun givenProvidedTextItShouldAddMenuItem() {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returns 0
                val textKey = TextKey("test")
                every { textProvider.getText(locale, textKey) } returns "Hi there"

                menu.addItem(0, textKey)

                verify { nativeFunctionExecutor.addMenuItem(menuid = menuId.value, column = 0, menutext = "Hi there") }
            }

            @Test
            fun givenFormattedProvidedTextItShouldAddMenuItem() {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returns 0
                val textKey = TextKey("test")
                every { textProvider.getText(locale, textKey) } returns "Hi %s"
                every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

                menu.addItem(0, textKey, "there")

                verify { nativeFunctionExecutor.addMenuItem(menuid = menuId.value, column = 0, menutext = "Hi there") }
            }

            @ParameterizedTest
            @ValueSource(ints = [0, 1])
            fun givenNoRowItShouldAddItemAsNewRow(column: Int) {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returns 0

                val menuRow = menu.addItem(column, "Hi there")

                assertThat(menuRow)
                        .satisfies {
                            assertThat(it.menu)
                                    .isEqualTo(menu)
                            assertThat(it.index)
                                    .isEqualTo(0)
                            assertThat(it.getText(column))
                                    .isEqualTo("Hi there")
                        }
                assertThat(menu.rows)
                        .containsExactlyInAnyOrder(menuRow)
            }

            @Test
            fun givenItemIsAddedToExistingRowItShouldSetTheText() {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returns 0
                menu.addItem(0, "Hi there")

                val menuRow = menu.addItem(1, "How ya doing")

                assertThat(menuRow)
                        .satisfies {
                            assertThat(it.menu)
                                    .isEqualTo(menu)
                            assertThat(it.index)
                                    .isEqualTo(0)
                            assertThat(it.getText(0))
                                    .isEqualTo("Hi there")
                            assertThat(it.getText(1))
                                    .isEqualTo("How ya doing")
                        }
                assertThat(menu.rows)
                        .containsExactlyInAnyOrder(menuRow)
            }

            @Test
            fun shouldAddItemToNewRow() {
                every { nativeFunctionExecutor.addMenuItem(any(), any(), any()) } returnsMany listOf(0, 1)
                val existingMenuRow = menu.addItem(0, "Hi there")

                val newMenuRow = menu.addItem(0, "How ya doing")

                assertThat(existingMenuRow)
                        .satisfies {
                            assertThat(it.menu)
                                    .isEqualTo(menu)
                            assertThat(it.index)
                                    .isEqualTo(0)
                            assertThat(it.getText(0))
                                    .isEqualTo("Hi there")
                            assertThat(it.getText(1))
                                    .isNull()
                        }
                assertThat(newMenuRow)
                        .satisfies {
                            assertThat(it.menu)
                                    .isEqualTo(menu)
                            assertThat(it.index)
                                    .isEqualTo(1)
                            assertThat(it.getText(0))
                                    .isEqualTo("How ya doing")
                            assertThat(it.getText(1))
                                    .isNull()
                        }
                assertThat(menu.rows)
                        .containsExactly(existingMenuRow, newMenuRow)
            }
        }

        @Nested
        inner class SetColumnHeaderTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.setMenuColumnHeader(any(), any(), any()) } returns true
            }

            @ParameterizedTest
            @ValueSource(ints = [0, 1])
            fun shouldSetColumnHeader(column: Int) {
                menu.setColumnHeader(column, "Hi there")

                verify {
                    nativeFunctionExecutor.setMenuColumnHeader(
                            menuid = menuId.value,
                            column = column,
                            columnheader = "Hi there"
                    )
                }
            }

            @ParameterizedTest
            @ValueSource(ints = [-1, 2])
            fun givenInvalidColumnItShouldThrowException(column: Int) {
                val caughtThrowable = catchThrowable { menu.setColumnHeader(column, "Hi there") }

                assertThat(caughtThrowable)
                        .isInstanceOf(IllegalArgumentException::class.java)
            }

            @Test
            fun givenFormattedTextItShouldSetColumnHeader() {
                every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

                menu.setColumnHeader(0, "Hi %s", "there")

                verify {
                    nativeFunctionExecutor.setMenuColumnHeader(
                            menuid = menuId.value,
                            column = 0,
                            columnheader = "Hi there"
                    )
                }
            }

            @Test
            fun givenProvidedTextItShouldSetColumnHeader() {
                val textKey = TextKey("test")
                every { textProvider.getText(locale, textKey) } returns "Hi there"

                menu.setColumnHeader(0, textKey)

                verify {
                    nativeFunctionExecutor.setMenuColumnHeader(
                            menuid = menuId.value,
                            column = 0,
                            columnheader = "Hi there"
                    )
                }
            }

            @Test
            fun givenFormattedProvidedTextItShouldSetColumnHeader() {
                val textKey = TextKey("test")
                every { textProvider.getText(locale, textKey) } returns "Hi %s"
                every { textFormatter.format(locale, "Hi %s", "there") } returns "Hi there"

                menu.setColumnHeader(0, textKey, "there")

                verify {
                    nativeFunctionExecutor.setMenuColumnHeader(
                            menuid = menuId.value,
                            column = 0,
                            columnheader = "Hi there"
                    )
                }
            }
        }

        @Test
        fun shouldDisableMenu() {
            every { nativeFunctionExecutor.disableMenu(any()) } returns true

            menu.disable()

            verify { nativeFunctionExecutor.disableMenu(menuId.value) }
        }

        @Test
        fun shouldShowForPlayer() {
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(69)
            }
            every { nativeFunctionExecutor.showMenuForPlayer(any(), any()) } returns true

            menu.show(player)

            verify { nativeFunctionExecutor.showMenuForPlayer(menuid = menuId.value, playerid = 69) }
        }

        @Test
        fun shouldHideForPlayer() {
            val player = mockk<Player> {
                every { id } returns PlayerId.valueOf(69)
            }
            every { nativeFunctionExecutor.hideMenuForPlayer(any(), any()) } returns true

            menu.hide(player)

            verify { nativeFunctionExecutor.hideMenuForPlayer(menuid = menuId.value, playerid = 69) }
        }

        @Test
        fun shouldCallOnPlayerExitedMenuReceiverDelegate() {
            val player = mockk<Player>()
            every { onPlayerExitedMenuReceiver.onPlayerExitedMenu(any(), any()) } just Runs

            menu.onExit(player)

            verify { onPlayerExitedMenuReceiver.onPlayerExitedMenu(player, menu) }
        }

        @Nested
        inner class DestroyTests {

            @BeforeEach
            fun setUp() {
                every { nativeFunctionExecutor.destroyMenu(any()) } returns true
            }

            @Test
            fun isDestroyedShouldInitiallyBeFalse() {
                val isDestroyed = menu.isDestroyed

                assertThat(isDestroyed)
                        .isFalse()
            }

            @Test
            fun shouldDestroyMenu() {
                val onDestroy = mockk<Menu.() -> Unit>(relaxed = true)
                menu.onDestroy(onDestroy)

                menu.destroy()

                verifyOrder {
                    nativeFunctionExecutor.destroyMenu(menuId.value)
                    onDestroy.invoke(menu)
                }
                assertThat(menu.isDestroyed)
                        .isTrue()
            }

            @Test
            fun shouldNotExecuteDestroyTwice() {
                val onDestroy = mockk<Menu.() -> Unit>(relaxed = true)
                menu.onDestroy(onDestroy)

                menu.destroy()
                menu.destroy()

                verify(exactly = 1) {
                    nativeFunctionExecutor.destroyMenu(menuId.value)
                    onDestroy.invoke(menu)
                }
            }

            @Test
            fun givenItDestroyedIdShouldThrowException() {
                menu.destroy()

                val caughtThrowable = catchThrowable { menu.id }

                assertThat(caughtThrowable)
                        .isInstanceOf(AlreadyDestroyedException::class.java)
            }
        }
    }

}