package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.CommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandErrorHandler
import ch.leadrian.samp.kamp.core.api.command.DefaultUnknownCommandHandler
import ch.leadrian.samp.kamp.core.api.command.UnknownCommandHandler
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CommandProcessor
@Inject
constructor(
        private val commandParser: CommandParser,
        private val commandRegistry: CommandRegistry,
        private val commandAccessCheckExecutor: CommandAccessCheckExecutor,
        private val commandParametersResolver: CommandParametersResolver,
        private val commandMethodInvoker: CommandMethodInvoker,
        private val callbackListenerManager: CallbackListenerManager,
        defaultUnknownCommandHandler: DefaultUnknownCommandHandler,
        defaultCommandErrorHandler: DefaultCommandErrorHandler
) : OnPlayerCommandTextListener {

    private companion object {

        val log = loggerFor<CommandProcessor>()

    }

    @field:com.google.inject.Inject(optional = true)
    private var unknownCommandHandler: UnknownCommandHandler = defaultUnknownCommandHandler

    @field:com.google.inject.Inject(optional = true)
    private var commandErrorHandler: CommandErrorHandler = defaultCommandErrorHandler

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerCommandText(player: Player, commandText: String): OnPlayerCommandTextListener.Result {
        try {
            val parsedCommand = commandParser.parse(commandText)
                    ?: return commandErrorHandler.handle(player, commandText, null)

            val commandDefinition = getCommandDefinition(parsedCommand)
                    ?: return unknownCommandHandler.handle(player, commandText)

            val stringParameterValues = getStringParameterValues(commandDefinition, parsedCommand)

            return processCommand(player, commandText, commandDefinition, stringParameterValues)
        } catch (e: Exception) {
            log.error("Exception while processing command by player {}: {}", player.name, commandText, e)
            return commandErrorHandler.handle(player, commandText, e)
        }
    }

    private fun getCommandDefinition(parsedCommand: ParsedCommand) =
            commandRegistry.getCommandDefinition(parsedCommand.command, parsedCommand.parameterValues.firstOrNull())

    private fun getStringParameterValues(
            commandDefinition: CommandDefinition,
            parsedCommand: ParsedCommand
    ): List<String> =
            when {
                commandDefinition.groupName == null -> parsedCommand.parameterValues
                else -> parsedCommand.parameterValues.drop(1)
            }

    private fun processCommand(
            player: Player,
            commandLine: String,
            commandDefinition: CommandDefinition,
            stringParameterValues: List<String>
    ): OnPlayerCommandTextListener.Result {
        commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)?.let {
            return when (it) {
                OnPlayerCommandTextListener.Result.UnknownCommand -> unknownCommandHandler.handle(player, commandLine)
                else -> OnPlayerCommandTextListener.Result.Processed
            }
        }
        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)
        return when (result) {
            is CommandParametersResolver.Result.ParameterValues -> commandMethodInvoker.invoke(
                    commandDefinition,
                    result.parameterValues
            )
            is CommandParametersResolver.Result.Error -> result.returnValue
        }
    }

}