package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class CommandParameterResolverProvider
@Inject
constructor(private val commandParameterResolverRegistry: CommandParameterResolverRegistry) {

    fun <T : Any> getResolver(parameterType: Class<T>): CommandParameterResolver<out T> {
        return commandParameterResolverRegistry.getResolver(parameterType)
                ?: getStringConstructorBasedResolver(parameterType)
                ?: getFactoryMethodBasedResolver(parameterType)
                ?: throw IllegalArgumentException("No command parameter resolver for type ${parameterType.name}")
    }

    private fun <T : Any> getStringConstructorBasedResolver(parameterType: Class<T>): StringConstructorBasedCommandParameterResolver<T>? {
        return StringConstructorBasedCommandParameterResolver.forType(parameterType)?.apply {
            commandParameterResolverRegistry.register(this)
        }
    }

    private fun <T : Any> getFactoryMethodBasedResolver(parameterType: Class<T>): FactoryMethodBasedCommandParameterResolver<T>? {
        return FactoryMethodBasedCommandParameterResolver.forType(parameterType)?.apply {
            commandParameterResolverRegistry.register(this)
        }
    }

}
