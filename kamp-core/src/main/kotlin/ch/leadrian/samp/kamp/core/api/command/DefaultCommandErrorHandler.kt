package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject

/**
 * Default [CommandErrorHandler] that will be used if none other was provided through a [ch.leadrian.samp.kamp.core.api.inject.KampModule].
 */
open class DefaultCommandErrorHandler
@Inject
constructor(private val messageSender: MessageSender) : CommandErrorHandler {

    /**
     * If [exception] is not null, a message saying that an unknown error occurred will be sent to the player,
     * else, a parsing error must have occurred and a message saying that the command was not valid will be sent to the player.
     * Any further command processing will be suppress by returning [OnPlayerCommandTextListener.Result.Processed].
     */
    override fun handle(
            player: Player,
            commandLine: String,
            exception: Exception?
    ): OnPlayerCommandTextListener.Result {
        when (exception) {
            null -> messageSender.sendMessageToPlayer(player, Colors.RED, KampCoreTextKeys.command.invalid, commandLine)
            else -> messageSender.sendMessageToPlayer(player, Colors.RED, KampCoreTextKeys.command.unexpected.error)
        }
        return OnPlayerCommandTextListener.Result.Processed
    }
}