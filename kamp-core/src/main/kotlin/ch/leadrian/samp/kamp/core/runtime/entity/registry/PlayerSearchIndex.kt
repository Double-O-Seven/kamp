package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerNameChangeListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import org.apache.commons.collections4.Trie
import org.apache.commons.collections4.trie.PatriciaTrie
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerSearchIndex
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerNameChangeListener,
        OnPlayerDisconnectListener, OnPlayerConnectListener {

    private val playersByName: Trie<String, Player> = PatriciaTrie()

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerConnect(player: Player) {
        addToIndex(player.name.toLowerCase(), player)
    }

    override fun onPlayerNameChange(player: Player, oldName: String, newName: String) {
        removeFromIndex(oldName.toLowerCase(), player)
        addToIndex(newName.toLowerCase(), player)
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        removeFromIndex(player.name.toLowerCase(), player)
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