package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

interface UnknownCommandHandler {

    fun handle(player: Player, command: String, parameters: List<String>): OnPlayerCommandTextListener.Result

}