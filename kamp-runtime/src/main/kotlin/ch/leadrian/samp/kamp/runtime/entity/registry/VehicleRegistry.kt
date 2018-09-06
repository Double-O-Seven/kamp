package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.runtime.entity.VehicleImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VehicleRegistry
@Inject
constructor() : EntityRegistry<VehicleImpl, VehicleId>(
        arrayOfNulls(SAMPConstants.MAX_VEHICLES)
)
