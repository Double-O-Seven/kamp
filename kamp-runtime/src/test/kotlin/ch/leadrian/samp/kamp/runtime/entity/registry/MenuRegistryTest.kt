package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Menu
import ch.leadrian.samp.kamp.api.entity.id.MenuId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MenuRegistryTest {

    @ParameterizedTest
    @ValueSource(ints = [0, SAMPConstants.MAX_MENUS - 1])
    fun shouldRegisterAndGetMenu(menuId: Int) {
        val menu = mockk<Menu> {
            every { id } returns MenuId.valueOf(menuId)
        }
        val menuRegistry = MenuRegistry()

        menuRegistry.register(menu)

        val registeredMenu = menuRegistry[menuId]
        assertThat(registeredMenu)
                .isSameAs(menu)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, SAMPConstants.MAX_MENUS, SAMPConstants.MAX_MENUS + 1])
    fun givenUnknownMenuIdGetMenuShouldReturn(menuId: Int) {
        val menuRegistry = MenuRegistry()

        val registeredMenu = menuRegistry[menuId]
        assertThat(registeredMenu)
                .isNull()
    }

    @Test
    fun givenAnotherMenuWithTheSameIdIsAlreadyRegisteredRegisterShouldThrowAnException() {
        val menuId = 50
        val alreadyRegisteredMenu = mockk<Menu> {
            every { id } returns MenuId.valueOf(menuId)
        }
        val newMenu = mockk<Menu> {
            every { id } returns MenuId.valueOf(menuId)
        }
        val menuRegistry = MenuRegistry()
        menuRegistry.register(alreadyRegisteredMenu)

        val caughtThrowable = catchThrowable { menuRegistry.register(newMenu) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredMenu = menuRegistry[menuId]
        assertThat(registeredMenu)
                .isSameAs(alreadyRegisteredMenu)
    }

    @Test
    fun shouldUnregisterRegisteredMenu() {
        val menuId = 50
        val menu = mockk<Menu> {
            every { id } returns MenuId.valueOf(menuId)
        }
        val menuRegistry = MenuRegistry()
        menuRegistry.register(menu)

        menuRegistry.unregister(menu)

        val registeredMenu = menuRegistry[menuId]
        assertThat(registeredMenu)
                .isNull()
    }


    @Test
    fun givenMenuIsNotRegisteredItShouldThrowAnException() {
        val menuId = MenuId.valueOf(50)
        val menu = mockk<Menu> {
            every { id } returns menuId
        }
        val menuRegistry = MenuRegistry()

        val caughtThrowable = catchThrowable { menuRegistry.unregister(menu) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun givenAnotherMenuWithTheSameIdIsAlreadyRegisteredUnregisterShouldThrowAnException() {
        val menuId = 50
        val alreadyRegisteredMenu = mockk<Menu> {
            every { id } returns MenuId.valueOf(menuId)
        }
        val newMenu = mockk<Menu> {
            every { id } returns MenuId.valueOf(menuId)
        }
        val menuRegistry = MenuRegistry()
        menuRegistry.register(alreadyRegisteredMenu)

        val caughtThrowable = catchThrowable { menuRegistry.unregister(newMenu) }

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException::class.java)
        val registeredMenu = menuRegistry[menuId]
        assertThat(registeredMenu)
                .isSameAs(alreadyRegisteredMenu)
    }

    @Test
    fun shouldReturnAllRegisteredMenus() {
        val menuId1 = MenuId.valueOf(10)
        val menu1 = mockk<Menu> {
            every { id } returns menuId1
        }
        val menuId2 = MenuId.valueOf(15)
        val menu2 = mockk<Menu> {
            every { id } returns menuId2
        }
        val menuId3 = MenuId.valueOf(30)
        val menu3 = mockk<Menu> {
            every { id } returns menuId3
        }
        val menuRegistry = MenuRegistry()
        menuRegistry.register(menu1)
        menuRegistry.register(menu2)
        menuRegistry.register(menu3)

        val allMenus = menuRegistry.getAll()

        assertThat(allMenus)
                .containsExactly(menu1, menu2, menu3)
    }

}