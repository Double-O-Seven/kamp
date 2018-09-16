package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class PrimitiveLongCommandParameterResolver
@Inject
constructor() : CommandParameterResolver<Long> {

    override val parameterType: Class<Long> = Long::class.javaPrimitiveType!!

    override fun resolve(value: String): Long? = value.toLongOrNull()
}