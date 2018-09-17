package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import javax.inject.Inject

open class DefaultInvalidCommandParameterValueHandler
@Inject
constructor(
        private val messageSender: MessageSender,
        private val textProvider: TextProvider
) : InvalidCommandParameterValueHandler {

    override fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>,
            parameterIndex: Int?
    ): OnPlayerCommandTextListener.Result {
        val message = StringBuilder().apply {
            append(textProvider.getText(player.locale, TextKeys.command.usage.prefix))
            append(": /")
            append(commandDefinition.name)
            commandDefinition.parameters.forEach {
                append(" [")
                append(it.getName(player.locale))
                append(']')
            }
        }.toString()
        messageSender.sendMessageToPlayer(player, Colors.RED, message)
        return OnPlayerCommandTextListener.Result.Processed
    }
}