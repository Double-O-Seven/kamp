package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.entity.Actor
import ch.leadrian.samp.kamp.api.entity.id.ActorId

interface ActorService {

    fun getActor(actorId: ActorId): Actor

    fun getAllActors(): List<Actor>

    fun getPoolSize(): Int

}