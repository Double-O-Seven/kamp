package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.Menu
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.MenuRegistry
import javax.inject.Inject

internal class MenuCommandParameterResolver
@Inject
constructor(menuRegistry: MenuRegistry) : EntityCommandParameterResolver<Menu, MenuId>(menuRegistry) {

    override val parameterType: Class<Menu> = Menu::class.java

}