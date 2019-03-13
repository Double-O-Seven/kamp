package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.api.entity.registry.EntityRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ActorRegistry
@Inject
constructor() : EntityRegistry<Actor, ActorId>(arrayOfNulls(SAMPConstants.MAX_ACTORS))
