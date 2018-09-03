package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.id.ActorId
import javax.inject.Singleton

@Singleton
internal class ActorRegistry : EntityRegistry<Actor, ActorId>(arrayOfNulls(SAMPConstants.MAX_ACTORS))
