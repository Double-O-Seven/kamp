package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.VehicleId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.VehicleRegistry
import javax.inject.Inject

internal class VehicleCommandParameterResolver
@Inject
constructor(vehicleRegistry: VehicleRegistry) : EntityCommandParameterResolver<Vehicle, VehicleId>(vehicleRegistry) {

    override val parameterType: Class<Vehicle> = Vehicle::class.java

}