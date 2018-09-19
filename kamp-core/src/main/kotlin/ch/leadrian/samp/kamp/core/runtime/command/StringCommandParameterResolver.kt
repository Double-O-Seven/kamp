package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class StringCommandParameterResolver
@Inject
constructor() : CommandParameterResolver<String> {

    override val parameterType: Class<String> = String::class.java

    override fun resolve(value: String): String? = value
}