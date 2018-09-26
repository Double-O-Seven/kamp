package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.callback.OnPlayerCommandTextHandler
import javax.inject.Inject

abstract class Commands {

    @Inject
    private lateinit var onPlayerCommandTextHandler: OnPlayerCommandTextHandler

    var groupName: String? = null
        internal set

    lateinit var definitions: List<CommandDefinition>
        internal set

    fun showCommandList(title: String, player: Player, executeOnSelect: Boolean = true, maxCommandsPerPage: Int = 30) {
        TODO()
    }

    protected fun executeCommand(player: Player, commandText: String) {
        onPlayerCommandTextHandler.onPlayerCommandText(player, commandText)
    }

}