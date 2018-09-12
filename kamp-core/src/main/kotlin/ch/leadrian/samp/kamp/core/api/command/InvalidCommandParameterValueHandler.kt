package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

interface InvalidCommandParameterValueHandler {

    fun handle(
            player: Player,
            commandDefinition: CommandDefinition,
            parameters: List<String>,
            parameterIndex: Int
    ): OnPlayerCommandTextListener.Result

}