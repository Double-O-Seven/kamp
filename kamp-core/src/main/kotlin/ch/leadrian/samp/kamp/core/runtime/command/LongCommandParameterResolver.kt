package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class LongCommandParameterResolver
@Inject
constructor() : CommandParameterResolver<Long> {

    override val parameterType: Class<Long> = Long::class.javaObjectType

    override fun resolve(value: String): Long? = value.toLongOrNull()
}