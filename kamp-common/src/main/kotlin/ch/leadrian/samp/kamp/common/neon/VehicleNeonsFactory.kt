package ch.leadrian.samp.kamp.common.neon

import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import javax.inject.Inject
import kotlin.reflect.KClass

internal class VehicleNeonsFactory
@Inject
constructor(
        defaultVehicleNeonMapObjectProvider: DefaultVehicleNeonMapObjectProvider
) : EntityExtensionFactory<Vehicle, VehicleNeons> {

    @com.google.inject.Inject(optional = true)
    private var vehicleNeonMapObjectProvider: VehicleNeonMapObjectProvider = defaultVehicleNeonMapObjectProvider

    override val extensionClass: KClass<VehicleNeons> = VehicleNeons::class

    override fun create(entity: Vehicle): VehicleNeons =
            VehicleNeons(entity, vehicleNeonMapObjectProvider)

}