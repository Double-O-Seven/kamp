package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import java.lang.reflect.Method
import javax.inject.Inject

class DefaultCommandAccessDeniedHandler
@Inject
constructor() : CommandAccessDeniedHandler {

    override fun handle(
            player: Player,
            command: String,
            parameters: List<String>,
            method: Method
    ): OnPlayerCommandTextListener.Result = OnPlayerCommandTextListener.Result.UnknownCommand
}