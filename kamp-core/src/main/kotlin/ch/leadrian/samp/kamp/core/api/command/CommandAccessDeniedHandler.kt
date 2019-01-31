package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

/**
 * @see DefaultCommandAccessDeniedHandler
 */
interface CommandAccessDeniedHandler {

    /**
     * Handles access denied by a [CommandAccessChecker].
     * You may simply return [OnPlayerCommandTextListener.Result.UnknownCommand] to propagate
     * the access denial to a [UnknownCommandHandler].
     * Or you may send the player a message and return [OnPlayerCommandTextListener.Result.Processed] to skip any further command processing.
     */
    fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>
    ): OnPlayerCommandTextListener.Result

}