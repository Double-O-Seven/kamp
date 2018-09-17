package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.entity.Actor
import ch.leadrian.samp.kamp.core.api.entity.id.ActorId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.ActorRegistry
import javax.inject.Inject

internal class ActorCommandParameterResolver
@Inject
constructor(actorRegistry: ActorRegistry) : EntityCommandParameterResolver<Actor, ActorId>(actorRegistry) {

    override val parameterType: Class<Actor> = Actor::class.java

}