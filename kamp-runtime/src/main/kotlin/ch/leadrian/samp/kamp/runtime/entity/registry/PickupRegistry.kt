package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.PickupId
import ch.leadrian.samp.kamp.runtime.entity.PickupImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PickupRegistry
@Inject
constructor() : EntityRegistry<PickupImpl, PickupId>(arrayOfNulls(SAMPConstants.MAX_PICKUPS))
