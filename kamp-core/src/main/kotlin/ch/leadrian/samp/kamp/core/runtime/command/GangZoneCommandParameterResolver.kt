package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.GangZone
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.GangZoneRegistry
import javax.inject.Inject

internal class GangZoneCommandParameterResolver
@Inject
constructor(gangZoneRegistry: GangZoneRegistry) : EntityCommandParameterResolver<GangZone, GangZoneId>(gangZoneRegistry) {

    override val parameterType: Class<GangZone> = GangZone::class.java

}