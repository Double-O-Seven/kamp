package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.Pickup
import ch.leadrian.samp.kamp.core.api.entity.id.PickupId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PickupRegistry
import javax.inject.Inject

internal class PickupCommandParameterResolver
@Inject
constructor(pickupRegistry: PickupRegistry) : EntityCommandParameterResolver<Pickup, PickupId>(pickupRegistry) {

    override val parameterType: Class<Pickup> = Pickup::class.java

}