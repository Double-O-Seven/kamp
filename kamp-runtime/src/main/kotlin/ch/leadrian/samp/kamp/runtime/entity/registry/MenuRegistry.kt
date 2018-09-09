package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.MenuId
import ch.leadrian.samp.kamp.runtime.entity.MenuImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MenuRegistry
@Inject
constructor() : EntityRegistry<MenuImpl, MenuId>(arrayOfNulls(SAMPConstants.MAX_MENUS))
