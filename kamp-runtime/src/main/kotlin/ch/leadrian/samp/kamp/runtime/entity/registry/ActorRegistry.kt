package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.api.entity.Actor
import javax.inject.Singleton

@Singleton
internal class ActorRegistry {

    private val actors: Array<Actor?> = arrayOfNulls(SAMPConstants.MAX_ACTORS)

    fun register(actor: Actor) {
        if (actors[actor.id.value] != null) {
            throw IllegalStateException("There is already a actor with ID ${actor.id.value} registered")
        }
        actors[actor.id.value] = actor
    }

    fun unregister(actor: Actor) {
        if (actors[actor.id.value] !== actor) {
            throw IllegalStateException("Trying to unregister actor with ID ${actor.id.value} that is not registered")
        }
        actors[actor.id.value] = null
    }

    fun getActor(actorId: Int): Actor? =
            when (actorId) {
                in (0 until actors.size) -> actors[actorId]
                else -> null
            }

    fun getAllActors(): List<Actor> = actors.filterNotNull()

}