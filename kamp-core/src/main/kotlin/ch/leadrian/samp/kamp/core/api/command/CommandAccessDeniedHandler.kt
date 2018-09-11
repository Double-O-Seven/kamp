package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import java.lang.reflect.Method

interface CommandAccessDeniedHandler {

    fun handle(player: Player, command: String, parameters: List<String>, method: Method): OnPlayerCommandTextListener.Result

}