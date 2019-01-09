package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject

open class DefaultCommandErrorHandler
@Inject
constructor(private val messageSender: MessageSender) : CommandErrorHandler {

    override fun handle(
            player: Player,
            commandLine: String,
            exception: Exception?
    ): OnPlayerCommandTextListener.Result {
        when (exception) {
            null -> messageSender.sendMessageToPlayer(player, Colors.RED, TextKeys.command.invalid, commandLine)
            else -> messageSender.sendMessageToPlayer(player, Colors.RED, TextKeys.command.unexpected.error)
        }
        return OnPlayerCommandTextListener.Result.Processed
    }
}