package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.command.CommandAccessChecker
import ch.leadrian.samp.kamp.core.api.command.CommandDefinition
import ch.leadrian.samp.kamp.core.api.entity.Player

class VehiclesOnlyCommandAccessChecker : CommandAccessChecker {

    override fun hasAccess(player: Player, commandDefinition: CommandDefinition, parameters: List<String>): Boolean {
        return player.isInAnyVehicle
    }

}