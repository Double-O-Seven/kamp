package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class FloatCommandParameterResolver
@Inject
constructor() : CommandParameterResolver<Float> {

    override val parameterType: Class<Float> = Float::class.javaObjectType

    override fun resolve(value: String): Float? = value.toFloatOrNull()
}