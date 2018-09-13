package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player

interface CommandErrorHandler {

    fun handle(player: Player, commandLine: String): OnPlayerCommandTextListener.Result

}