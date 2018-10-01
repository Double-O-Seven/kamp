package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.MenuFactory
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import java.util.*
import javax.inject.Inject

class MenuService
@Inject
internal constructor(
        private val menuFactory: MenuFactory,
        private val menuRegistry: MenuRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider
) {

    fun createSingleColumnMenu(
            position: Vector2D,
            columnWidth: Float,
            title: String,
            locale: Locale = Locale.getDefault()
    ): Menu = menuFactory.create(position, columnWidth, title, locale)

    fun createSingleColumnMenu(
            titleTextKey: TextKey,
            position: Vector2D,
            columnWidth: Float,
            locale: Locale = Locale.getDefault()
    ): Menu {
        val title = textProvider.getText(locale, titleTextKey)
        return createSingleColumnMenu(position, columnWidth, title, locale)
    }

    fun createDoubleColumnMenu(
            position: Vector2D,
            columnWidth1: Float,
            columnWidth2: Float,
            title: String,
            locale: Locale = Locale.getDefault()
    ): Menu = menuFactory.create(position, columnWidth1, columnWidth2, title, locale)

    fun createDoubleColumnMenu(
            titleTextKey: TextKey,
            position: Vector2D,
            columnWidth1: Float,
            columnWidth2: Float,
            locale: Locale = Locale.getDefault()
    ): Menu {
        val title = textProvider.getText(locale, titleTextKey)
        return createDoubleColumnMenu(position, columnWidth1, columnWidth2, title, locale)
    }

    fun isValid(menuId: MenuId): Boolean = nativeFunctionExecutor.isValidMenu(menuId.value)

    fun getMenu(menuId: MenuId): Menu =
            menuRegistry[menuId] ?: throw NoSuchEntityException("No menu with ID ${menuId.value}")

    fun getAllMenus(): List<Menu> = menuRegistry.getAll()

}