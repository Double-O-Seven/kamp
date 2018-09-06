package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.GangZone
import ch.leadrian.samp.kamp.api.entity.id.GangZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GangZoneRegistry
@Inject
constructor() : EntityRegistry<GangZone, GangZoneId>(arrayOfNulls(SAMPConstants.MAX_GANG_ZONES))
