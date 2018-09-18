package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject

class AdminCommandAccessChecker
@Inject
constructor() : CommandAccessChecker {

    override fun hasAccess(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean =
            player.isAdmin
}