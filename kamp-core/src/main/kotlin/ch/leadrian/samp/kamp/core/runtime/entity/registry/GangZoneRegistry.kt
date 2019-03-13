package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.GangZone
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GangZoneRegistry
@Inject
constructor() : EntityRegistry<GangZone, GangZoneId>(arrayOfNulls(SAMPConstants.MAX_GANG_ZONES))
