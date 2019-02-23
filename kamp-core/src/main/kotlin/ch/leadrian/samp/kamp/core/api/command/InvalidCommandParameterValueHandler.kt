package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

/**
 * Handles invalid command parameter input given by the player.
 * A command or it's parameters defined in a [Commands] implementation may be annotation with
 * [ch.leadrian.samp.kamp.core.api.command.annotation.InvalidParameterValueHandler] in order to
 * specify a [InvalidCommandParameterValueHandler] for the given command or parameter.
 *
 * @see [ch.leadrian.samp.kamp.core.api.command.annotation.InvalidParameterValueHandler]
 */
interface InvalidCommandParameterValueHandler {

    /**
     * @return [OnPlayerCommandTextListener.Result.Processed] if no further command processing should be done, else [OnPlayerCommandTextListener.Result.UnknownCommand].
     */
    fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>,
            parameterIndex: Int?
    ): OnPlayerCommandTextListener.Result

}