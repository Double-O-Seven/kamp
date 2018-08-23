package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.runtime.entity.PlayerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerRegistry
@Inject
constructor(nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    private val players: Array<PlayerImpl?> = arrayOfNulls(nativeFunctionExecutor.getMaxPlayers())

    fun register(player: PlayerImpl) {
        if (players[player.id.value] != null) {
            throw IllegalStateException("There is already a player with ID ${player.id.value} registered")
        }
        players[player.id.value] = player
    }

    fun unregister(player: PlayerImpl) {
        if (players[player.id.value] !== player) {
            throw IllegalStateException("Trying to unregister player with ID ${player.id.value} that is not registered")
        }
        players[player.id.value] = null
    }

    fun getPlayer(playerId: Int): PlayerImpl? =
            when (playerId) {
                in (0 until players.size) -> players[playerId]
                else -> null
            }

    fun getAllPlayers(): List<PlayerImpl> = players.filterNotNull()

}