package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.entity.VehicleImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VehicleRegistry
@Inject
constructor() : EntityRegistry<VehicleImpl, VehicleId>(
        arrayOfNulls(SAMPConstants.MAX_VEHICLES)
)
