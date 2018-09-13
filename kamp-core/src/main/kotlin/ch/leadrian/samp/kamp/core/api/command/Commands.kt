package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.command.CommandProcessor

abstract class Commands {

    var groupName: String? = null
        internal set

    lateinit var definitions: List<CommandDefinition>
        internal set

    internal lateinit var commandProcessor: CommandProcessor

    fun showCommandList(title: String, player: Player, executeOnSelect: Boolean = true) {
        TODO()
    }

}