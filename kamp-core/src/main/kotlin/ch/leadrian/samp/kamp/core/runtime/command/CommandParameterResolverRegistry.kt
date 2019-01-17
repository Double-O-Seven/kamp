package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CommandParameterResolverRegistry
@Inject
constructor(resolvers: Set<@JvmSuppressWildcards CommandParameterResolver<*>>) {

    private companion object {

        val log = loggerFor<CommandParameterResolverRegistry>()

    }

    private val resolversByParameterType: MutableMap<Class<*>, CommandParameterResolver<*>> = resolvers.associateBy { it.parameterType }
            .toMutableMap()

    @PostConstruct
    fun printResolvers() {
        resolversByParameterType.forEach { type, resolver ->
            log.info("Registered {} as resolver for type {}", resolver::class.qualifiedName, type)
        }
    }

    fun <T : Any> getResolver(parameterType: Class<T>): CommandParameterResolver<out T>? {
        @Suppress("UNCHECKED_CAST")
        return resolversByParameterType[parameterType] as CommandParameterResolver<out T>?
    }

    fun register(resolver: CommandParameterResolver<*>) {
        resolversByParameterType[resolver.parameterType] = resolver
    }

}