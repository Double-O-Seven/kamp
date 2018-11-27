package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import javax.inject.Inject


internal class CommandExecutor
@Inject
constructor() {

    fun execute(commandDefinition: CommandDefinition, parameterValues: Array<Any>): OnPlayerCommandTextListener.Result {
        val result = commandDefinition.method.invoke(commandDefinition.commandsInstance, *parameterValues)
        return when {
            commandDefinition.method.returnType == Unit::class.java -> OnPlayerCommandTextListener.Result.Processed
            commandDefinition.method.returnType == Void::class.javaPrimitiveType -> OnPlayerCommandTextListener.Result.Processed
            result is OnPlayerCommandTextListener.Result -> result
            result is Boolean || commandDefinition.method.returnType == Boolean::class.javaObjectType -> {
                if ((result as Boolean?) == true) {
                    OnPlayerCommandTextListener.Result.Processed
                } else {
                    OnPlayerCommandTextListener.Result.UnknownCommand
                }
            }
            else -> OnPlayerCommandTextListener.Result.Processed
        }
    }

}