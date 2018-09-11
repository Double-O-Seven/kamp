package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import javax.inject.Inject

internal class PickupFactory
@Inject
constructor(
        private val pickupRegistry: PickupRegistry,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun create(
            modelId: Int,
            type: Int,
            coordinates: Vector3D,
            virtualWorldId: Int?
    ): Pickup {
        val pickup = Pickup(
                modelId = modelId,
                type = type,
                coordinates = coordinates,
                virtualWorldId = virtualWorldId,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
        pickupRegistry.register(pickup)
        pickup.onDestroy { pickupRegistry.unregister(this) }
        return pickup
    }

}