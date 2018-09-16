package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CommandParameterResolverRegistry
@Inject
constructor(resolvers: Set<@JvmSuppressWildcards CommandParameterResolver<*>>) {

    private val resolversByParameterType: Map<Class<*>, CommandParameterResolver<*>> = resolvers.associateBy { it.parameterType }

    fun <T : Any> getResolver(parameterType: Class<T>): CommandParameterResolver<out T> {
        @Suppress("UNCHECKED_CAST")
        val resolver = resolversByParameterType[parameterType] as CommandParameterResolver<out T>?
        return resolver ?: throw NoSuchElementException("No resolver for type $parameterType registered")
    }

}