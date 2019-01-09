package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.command.CommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.command.DefaultCommandAccessDeniedHandler
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject

internal class CommandAccessCheckExecutor
@Inject
constructor(
        private val messageSender: MessageSender,
        defaultCommandAccessDeniedHandler: DefaultCommandAccessDeniedHandler
) {

    @field:com.google.inject.Inject(optional = true)
    private var defaultCommandAccessDeniedHandler: CommandAccessDeniedHandler = defaultCommandAccessDeniedHandler

    fun checkAccess(player: Player, commandDefinition: CommandDefinition, stringParameterValues: List<String>): OnPlayerCommandTextListener.Result? {
        commandDefinition.accessCheckers.forEach { accessCheckerGroup ->
            val accessDenied = accessCheckerGroup.accessCheckers.any {
                !it.hasAccess(player, commandDefinition, stringParameterValues)
            }
            if (accessDenied) {
                val isProcessed = accessCheckerGroup.accessDeniedHandlers.any {
                    it.handle(player, commandDefinition, stringParameterValues) == OnPlayerCommandTextListener.Result.Processed
                }
                if (isProcessed) {
                    return OnPlayerCommandTextListener.Result.Processed
                }
                val message = accessCheckerGroup.getErrorMessage(player.locale)
                if (message != null) {
                    messageSender.sendMessageToPlayer(player, Colors.RED, message)
                    return OnPlayerCommandTextListener.Result.Processed
                }
                return defaultCommandAccessDeniedHandler.handle(player, commandDefinition, stringParameterValues)
            }
        }
        return null
    }

}