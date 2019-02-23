package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.MessageSender
import javax.inject.Inject

/**
 * Default [UnknownCommandHandler] if none other was provided.
 */
open class DefaultUnknownCommandHandler
@Inject
constructor(private val messageSender: MessageSender) : UnknownCommandHandler {

    /**
     * Simply shows a red message to the player, saying that the command is unknown.
     *
     * Example:
     * ```
     * Unknown command: /makemeadmin
     * ```
     */
    override fun handle(player: Player, commandLine: String): OnPlayerCommandTextListener.Result {
        messageSender.sendMessageToPlayer(
                player,
                Colors.RED,
                KampCoreTextKeys.command.unknown,
                commandLine
        )
        return OnPlayerCommandTextListener.Result.Processed
    }
}