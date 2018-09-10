package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MenuRowImplTest {

    private val menuId = MenuId.valueOf(69)
    private val menu = mockk<Menu>()
    private lateinit var menuRow: MenuRowImpl

    private val nativeFunctionExecutor = mockk<SAMPNativeFunctionExecutor>()

    @BeforeEach
    fun setUp() {
        every { menu.id } returns menuId
        every { menu.numberOfColumns } returns 2
        menuRow = MenuRowImpl(menu, 3, nativeFunctionExecutor)
    }

    @Test
    fun shouldDisableMenuRow() {
        every { nativeFunctionExecutor.disableMenuRow(any(), any()) } returns true

        menuRow.disable()

        verify { nativeFunctionExecutor.disableMenuRow(menuid = menuId.value, row = 3) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun shouldInitializeTextsWithNull(column: Int) {
        val text = menuRow.getText(column)

        assertThat(text)
                .isNull()
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1])
    fun shouldSetText(column: Int) {
        menuRow.setText(column, "Test 123")

        assertThat(menuRow.getText(column))
                .isEqualTo("Test 123")
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 2])
    fun givenInvalidColumnSetTextShouldThrowAnException(column: Int) {
        val caughtThrowable = catchThrowable { menuRow.setText(column, "Test 123") }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 2])
    fun givenInvalidColumnGetTextShouldThrowAnException(column: Int) {
        val caughtThrowable = catchThrowable { menuRow.getText(column) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun shouldExecuteOnSelectedHandlers() {
        val onSelected = mockk<MenuRow.(Player) -> Unit>(relaxed = true)
        val player = mockk<Player>()
        menuRow.onSelected(onSelected)

        menuRow.onSelected(player)

        verify { onSelected.invoke(menuRow, player) }
    }

}