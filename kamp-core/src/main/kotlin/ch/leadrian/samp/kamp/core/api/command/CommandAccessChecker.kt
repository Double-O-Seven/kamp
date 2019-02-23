package ch.leadrian.samp.kamp.core.api.command

import ch.leadrian.samp.kamp.core.api.entity.Player

/**
 * Performs an access check for a given command.
 *
 * Annotate a [Commands] class or a specific command function with [ch.leadrian.samp.kamp.core.api.command.annotation.AccessCheck]
 * or [ch.leadrian.samp.kamp.core.api.command.annotation.AccessChecks] to apply the [CommandAccessChecker].
 *
 * @see [ch.leadrian.samp.kamp.core.api.command.annotation.AccessCheck]
 * @see [ch.leadrian.samp.kamp.core.api.command.annotation.AccessChecks]
 */
interface CommandAccessChecker {

    /**
     * @return true if the player has access to the command, else false
     */
    fun hasAccess(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean

}