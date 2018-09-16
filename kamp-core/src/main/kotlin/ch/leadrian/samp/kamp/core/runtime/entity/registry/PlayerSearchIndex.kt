package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.Player
import org.apache.commons.collections4.Trie
import org.apache.commons.collections4.trie.PatriciaTrie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerSearchIndex
@Inject
constructor() {

    private val playersByName: Trie<String, Player> = PatriciaTrie()

    fun index(player: Player) {
        addToIndex(player.name.toLowerCase(), player)
        player.onNameChange { oldName, newName ->
            removeFromIndex(oldName.toLowerCase(), this)
            addToIndex(newName.toLowerCase(), this)
        }
        player.onDisconnect { removeFromIndex(player.name.toLowerCase(), this) }
    }

    private fun addToIndex(lowerCaseName: String, player: Player) {
        player.requireConnected()
        if (playersByName.containsKey(lowerCaseName)) {
            throw IllegalStateException("Player with name ${player.name} is already indexed")
        }

        playersByName[lowerCaseName] = player
    }

    private fun removeFromIndex(lowerCaseName: String, player: Player) {
        playersByName.remove(lowerCaseName, player)
    }

    fun getPlayer(name: String): Player? = playersByName[name.toLowerCase()]

    fun findPlayers(partialName: String): List<Player> = playersByName.prefixMap(partialName.toLowerCase()).values.toList()

}