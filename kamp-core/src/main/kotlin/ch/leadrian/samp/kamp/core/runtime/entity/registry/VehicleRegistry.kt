package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class VehicleRegistry
@Inject
constructor() : EntityRegistry<Vehicle, VehicleId>(
        arrayOfNulls(SAMPConstants.MAX_VEHICLES)
)
