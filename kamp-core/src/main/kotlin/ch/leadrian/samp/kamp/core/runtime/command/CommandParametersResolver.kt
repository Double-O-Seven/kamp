package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.DefaultInvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.command.InvalidCommandParameterValueHandler
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject

internal class CommandParametersResolver
@Inject
constructor(defaultInvalidCommandParameterValueHandler: DefaultInvalidCommandParameterValueHandler) {

    @field:com.google.inject.Inject(optional = true)
    private var defaultInvalidCommandParameterValueHandler: InvalidCommandParameterValueHandler = defaultInvalidCommandParameterValueHandler

    fun resolve(
            player: Player,
            commandDefinition: CommandDefinition,
            stringParameterValues: List<String>
    ): Result {
        val numberOfParameters = commandDefinition.parameters.size
        if (stringParameterValues.size < numberOfParameters) {
            return handleMissingParameterValue(player, commandDefinition, stringParameterValues)
        }

        val parameterValues = arrayOfNulls<Any>(1 + numberOfParameters)
        parameterValues[0] = player
        (0 until numberOfParameters).forEach { i ->
            parameterValues[i + 1] = resolve(commandDefinition, stringParameterValues, i)
                    ?: return handleInvalidParameterValue(player, commandDefinition, stringParameterValues, i)
        }
        @Suppress("UNCHECKED_CAST")
        return Result.ParameterValues(parameterValues as Array<Any>)
    }

    private fun resolve(commandDefinition: CommandDefinition, stringParameterValues: List<String>, index: Int): Any? {
        val resolver = commandDefinition.parameters[index].resolver
        if (index < commandDefinition.parameters.size - 1) {
            return resolver.resolve(stringParameterValues[index])
        } else {
            val parameterType = commandDefinition.parameters[index].type
            when {
                parameterType == String::class.java && commandDefinition.isGreedy -> {
                    val greedyValue = (index until stringParameterValues.size).joinToString(separator = " ") {
                        stringParameterValues[it]
                    }
                    return resolver.resolve(greedyValue)
                }
                parameterType == Iterable::class.java || parameterType == List::class.java || parameterType == Collection::class.java -> {
                    return (index until stringParameterValues.size).map { i ->
                        resolver.resolve(stringParameterValues[i]) ?: return null
                    }.toList()
                }
                parameterType == Set::class.java -> {
                    return (index until stringParameterValues.size).map { i ->
                        resolver.resolve(stringParameterValues[i]) ?: return null
                    }.toSet()
                }
                else -> return resolver.resolve(stringParameterValues[index])
            }
        }
    }

    private fun handleMissingParameterValue(
            player: Player,
            commandDefinition: CommandDefinition,
            stringParameterValues: List<String>
    ): Result.Error =
            defaultInvalidCommandParameterValueHandler.handle(
                    player,
                    commandDefinition,
                    stringParameterValues,
                    stringParameterValues.size
            ).let { Result.Error(it) }

    private fun handleInvalidParameterValue(
            player: Player,
            commandDefinition: CommandDefinition,
            stringParameterValues: List<String>,
            index: Int
    ): Result {
        val handler = commandDefinition.parameters[index].invalidCommandParameterValueHandler
                ?: defaultInvalidCommandParameterValueHandler
        return handler.handle(player, commandDefinition, stringParameterValues, index).let { Result.Error(it) }
    }

    sealed class Result {

        class ParameterValues(val parameterValues: Array<Any>) : Result()

        class Error(val returnValue: OnPlayerCommandTextListener.Result) : Result()

    }
}