package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class IntCommandParameterResolver
@Inject
constructor() : CommandParameterResolver<Int> {

    override val parameterType: Class<Int> = Int::class.javaObjectType

    override fun resolve(value: String): Int? = value.toIntOrNull()
}