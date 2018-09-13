package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CommandParameterResolverRegistry
@Inject
constructor() {

    fun getResolver(clazz: Class<*>): CommandParameterResolver<*> {
        TODO()
    }

}