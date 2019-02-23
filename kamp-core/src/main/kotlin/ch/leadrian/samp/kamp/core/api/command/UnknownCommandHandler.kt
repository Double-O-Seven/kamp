package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

/**
 * Handles unknown commands or commands where access was denied to the player and the player should not know that the command exists.
 */
interface UnknownCommandHandler {

    /**
     * @return [OnPlayerCommandTextListener.Result.Processed] if no further command processing should be done, else [OnPlayerCommandTextListener.Result.UnknownCommand].
     */
    fun handle(player: Player, commandLine: String): OnPlayerCommandTextListener.Result

}