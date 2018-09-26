package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player

abstract class Commands {

    var groupName: String? = null
        internal set

    lateinit var definitions: List<CommandDefinition>
        internal set

    fun showCommandList(title: String, player: Player, executeOnSelect: Boolean = true) {
        TODO()
    }

}