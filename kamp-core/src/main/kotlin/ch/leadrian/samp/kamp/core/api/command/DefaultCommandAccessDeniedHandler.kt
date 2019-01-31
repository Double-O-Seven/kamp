package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject

/**
 * Default [CommandAccessDeniedHandler] that will be used if none other was provided through a [ch.leadrian.samp.kamp.core.api.inject.KampModule].
 */
open class DefaultCommandAccessDeniedHandler
@Inject
constructor() : CommandAccessDeniedHandler {

    /**
     * Does not process the command, command processing will be continued by an [UnknownCommandHandler].
     */
    override fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>
    ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.UnknownCommand
}