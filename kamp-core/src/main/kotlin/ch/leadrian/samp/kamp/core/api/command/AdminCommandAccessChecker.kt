package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject

/**
 * Allows command access if the player as an RCON admin.
 *
 * @see [CommandAccessChecker]
 */
class AdminCommandAccessChecker
@Inject
constructor() : CommandAccessChecker {

    override fun hasAccess(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean =
            player.isAdmin
}