package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Menu
import javax.inject.Singleton

@Singleton
internal class MenuRegistry {

    private val menus: Array<Menu?> = arrayOfNulls(SAMPConstants.MAX_MENUS)

    fun register(menu: Menu) {
        if (menus[menu.id.value] != null) {
            throw IllegalStateException("There is already a menu with ID ${menu.id.value} registered")
        }
        menus[menu.id.value] = menu
    }

    fun unregister(menu: Menu) {
        if (menus[menu.id.value] !== menu) {
            throw IllegalStateException("Trying to unregister menu with ID ${menu.id.value} that is not registered")
        }
        menus[menu.id.value] = null
    }

    fun getMenu(menuId: Int): Menu? =
            when (menuId) {
                in (0 until menus.size) -> menus[menuId]
                else -> null
            }

    fun getAllMenus(): List<Menu> = menus.filterNotNull()

}