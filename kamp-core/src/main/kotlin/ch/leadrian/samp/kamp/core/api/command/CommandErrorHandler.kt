package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

/**
 * Handles an error that occurred during command parsing or execution.
 *
 * An error can occur during parsing of the command or during the invocation of the command function.
 */
interface CommandErrorHandler {

    fun handle(player: Player, commandLine: String, exception: Exception?): OnPlayerCommandTextListener.Result

}