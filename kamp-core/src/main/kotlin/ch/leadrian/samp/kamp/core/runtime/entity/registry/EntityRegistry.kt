package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.id.EntityId
import java.util.LinkedList

internal abstract class EntityRegistry<T : Entity<U>, U : EntityId>(private val entities: Array<T?>) {

    private val entityList: LinkedList<T> = LinkedList()

    fun register(entity: T) {
        if (entities[entity.id.value] != null) {
            throw IllegalStateException("There is already an entity with ID ${entity.id.value} registered")
        }
        entities[entity.id.value] = entity
        entityList += entity
    }

    fun unregister(entity: T) {
        if (entities[entity.id.value] !== entity) {
            throw IllegalStateException("Trying to unregister entity with ID ${entity.id.value} that is not registered")
        }
        entities[entity.id.value] = null
        entityList -= entity
    }

    operator fun get(entityId: U): T? = get(entityId.value)

    operator fun get(entityId: Int): T? = entities.getOrNull(entityId)

    fun getAll(): List<T> = entityList.toList()

    val capacity: Int = entities.size

}