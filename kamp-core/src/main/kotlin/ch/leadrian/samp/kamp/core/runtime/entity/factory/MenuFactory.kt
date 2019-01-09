package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.onDestroy
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import java.util.Locale
import javax.inject.Inject

internal class MenuFactory
@Inject
constructor(
        private val menuRegistry: MenuRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) {

    fun create(
            position: Vector2D,
            columnWidth: Float,
            title: String,
            locale: Locale
    ): Menu = create(
            numberOfColumns = 1,
            position = position,
            columnWidth1 = columnWidth,
            columnWidth2 = 0f,
            title = title,
            locale = locale
    )

    fun create(
            position: Vector2D,
            columnWidth1: Float,
            columnWidth2: Float,
            title: String,
            locale: Locale
    ): Menu = create(
            numberOfColumns = 2,
            position = position,
            columnWidth1 = columnWidth1,
            columnWidth2 = columnWidth2,
            title = title,
            locale = locale
    )

    private fun create(
            numberOfColumns: Int,
            position: Vector2D,
            columnWidth1: Float,
            columnWidth2: Float,
            title: String,
            locale: Locale
    ): Menu {
        val menu = Menu(
                numberOfColumns = numberOfColumns,
                position = position,
                columnWidth1 = columnWidth1,
                columnWidth2 = columnWidth2,
                title = title,
                locale = locale,
                nativeFunctionExecutor = nativeFunctionExecutor,
                textProvider = textProvider,
                textFormatter = textFormatter
        )
        menuRegistry.register(menu)
        menu.onDestroy { menuRegistry.unregister(this) }
        return menu
    }

}