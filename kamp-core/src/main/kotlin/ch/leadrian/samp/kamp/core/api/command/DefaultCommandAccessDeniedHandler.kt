package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject

open class DefaultCommandAccessDeniedHandler
@Inject
constructor() : CommandAccessDeniedHandler {

    override fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>
    ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.UnknownCommand
}