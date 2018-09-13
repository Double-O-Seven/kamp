package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCommandTextListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CommandProcessor
@Inject constructor() : OnPlayerCommandTextListener {

    override fun onPlayerCommandText(player: Player, commandText: String): OnPlayerCommandTextListener.Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}