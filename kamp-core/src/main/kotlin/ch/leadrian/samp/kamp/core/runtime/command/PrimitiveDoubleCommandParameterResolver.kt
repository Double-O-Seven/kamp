package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import javax.inject.Inject

internal class PrimitiveDoubleCommandParameterResolver
@Inject
constructor() : CommandParameterResolver<Double> {

    override val parameterType: Class<Double> = Double::class.javaPrimitiveType!!

    override fun resolve(value: String): Double? = value.toDoubleOrNull()
}