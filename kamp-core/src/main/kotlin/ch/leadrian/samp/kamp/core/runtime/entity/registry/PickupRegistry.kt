package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.runtime.entity.PickupImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PickupRegistry
@Inject
constructor() : EntityRegistry<PickupImpl, PickupId>(arrayOfNulls(SAMPConstants.MAX_PICKUPS))
