package ch.leadrian.samp.kamp.core.runtime.command

import ch.leadrian.samp.kamp.core.api.command.CommandParameterResolver
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerSearchIndex
import javax.inject.Inject

internal class PlayerCommandParameterResolver
@Inject
constructor(
        private val playerRegistry: PlayerRegistry,
        private val playerSearchIndex: PlayerSearchIndex
) : CommandParameterResolver<Player> {

    override val parameterType: Class<Player> = Player::class.java

    override fun resolve(value: String): Player? {
        val playerId = value.toIntOrNull()
        return when {
            playerId != null -> playerRegistry[playerId]
            else -> getPlayerByName(value)
        }
    }

    private fun getPlayerByName(name: String): Player? {
        val players = playerSearchIndex.findPlayers(name)
        return when {
            players.size == 1 -> players.first()
            else -> null
        }
    }
}