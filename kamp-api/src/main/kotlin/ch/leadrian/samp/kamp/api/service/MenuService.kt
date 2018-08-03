package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Vector2D
import ch.leadrian.samp.kamp.api.entity.id.MenuId
import ch.leadrian.samp.kamp.api.text.TextKey
import java.awt.Menu

interface MenuService {

    fun createMenu(title: String, position: Vector2D, columnWidth: Float)

    fun createMenu(title: String, position: Vector2D, column1Width: Float, column2Width: Float)

    fun createMenu(titleTextKey: TextKey, position: Vector2D, columnWidth: Float)

    fun createMenu(titleTextKey: TextKey, position: Vector2D, column1Width: Float, column2Width: Float)

    fun isValid(menuId: MenuId): Boolean

    fun getMenu(menuId: MenuId): Menu

    fun getAllMenus(): List<Menu>

}