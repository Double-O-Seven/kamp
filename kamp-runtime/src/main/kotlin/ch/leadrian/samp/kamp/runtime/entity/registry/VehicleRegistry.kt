package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.runtime.entity.InterceptableVehicle
import javax.inject.Singleton

@Singleton
internal class VehicleRegistry : EntityRegistry<InterceptableVehicle, VehicleId>(
        arrayOfNulls(SAMPConstants.MAX_VEHICLES)
)
