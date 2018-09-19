package ch.leadrian.samp.kamp.core.runtime.command

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
        private val commandExecutor: CommandExecutor,
        defaultUnknownCommandHandler: DefaultUnknownCommandHandler,
        defaultCommandErrorHandler: DefaultCommandErrorHandler
) : OnPlayerCommandTextListener {

    private companion object {

        private val log = loggerFor<CommandProcessor>()

    }

    @field:com.google.inject.Inject(optional = true)
    private var unknownCommandHandler: UnknownCommandHandler = defaultUnknownCommandHandler

    @field:com.google.inject.Inject(optional = true)
    private var commandErrorHandler: CommandErrorHandler = defaultCommandErrorHandler

    @PostConstruct
    fun initialize() {
        TODO("register as callback listener")
    }

    override fun onPlayerCommandText(player: Player, commandText: String): OnPlayerCommandTextListener.Result {
        try {
            val parsedCommand = commandParser.parse(commandText)
                    ?: return commandErrorHandler.handle(player, commandText, null)

            val commandDefinition = getCommandDefinition(parsedCommand)
                    ?: return unknownCommandHandler.handle(player, parsedCommand.command, parsedCommand.parameterValues)

            val stringParameterValues = getStringParameterValues(commandDefinition, parsedCommand)

            return processCommand(player, commandDefinition, stringParameterValues)
        } catch (e: Exception) {
            log.error("Exception while processing command by player ${player.name}: $commandText", e)
            return commandErrorHandler.handle(player, commandText, e)
        }
    }

    private fun getCommandDefinition(parsedCommand: ParsedCommand) =
            commandRegistry.getCommandDefinition(parsedCommand.command, parsedCommand.parameterValues.firstOrNull())

    private fun getStringParameterValues(commandDefinition: CommandDefinition, parsedCommand: ParsedCommand): List<String> =
            when {
                commandDefinition.groupName == null -> parsedCommand.parameterValues
                else -> parsedCommand.parameterValues.drop(1)
            }

    private fun processCommand(
            player: Player,
            commandDefinition: CommandDefinition,
            stringParameterValues: List<String>
    ): OnPlayerCommandTextListener.Result {
        commandAccessCheckExecutor.checkAccess(player, commandDefinition, stringParameterValues)?.let { return it }
        val result = commandParametersResolver.resolve(player, commandDefinition, stringParameterValues)
        return when (result) {
            is CommandParametersResolver.Result.ParameterValues -> commandExecutor.execute(commandDefinition, result.parameterValues)
            is CommandParametersResolver.Result.Error -> result.returnValue
        }
    }


}