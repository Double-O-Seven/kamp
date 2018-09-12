package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player

interface Commands {

    val groupName: String?

    val definitions: List<CommandDefinition>

    fun showCommandList(title: String, player: Player, executeOnSelect: Boolean = true)

}