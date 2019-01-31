package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

/**
 * Handles unknown commands or commands where access was denied to the player and the player should not know that the command exists.
 */
interface UnknownCommandHandler {

    fun handle(player: Player, commandLine: String): OnPlayerCommandTextListener.Result

}