package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.id.EntityId
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistry

internal abstract class EntityCommandParameterResolver<T : Entity<U>, U : EntityId>(
        private val entityRegistry: EntityRegistry<T, U>
) : CommandParameterResolver<T> {

    override fun resolve(value: String): T? = value.toIntOrNull()?.let { entityRegistry[it] }

}