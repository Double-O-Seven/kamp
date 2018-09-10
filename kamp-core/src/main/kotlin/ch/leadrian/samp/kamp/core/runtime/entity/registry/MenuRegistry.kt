package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.MenuId
import ch.leadrian.samp.kamp.core.runtime.entity.MenuImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MenuRegistry
@Inject
constructor() : EntityRegistry<MenuImpl, MenuId>(arrayOfNulls(SAMPConstants.MAX_MENUS))
