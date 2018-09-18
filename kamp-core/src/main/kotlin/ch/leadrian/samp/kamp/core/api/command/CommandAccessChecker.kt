package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player

interface CommandAccessChecker {

    fun hasAccess(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean

}