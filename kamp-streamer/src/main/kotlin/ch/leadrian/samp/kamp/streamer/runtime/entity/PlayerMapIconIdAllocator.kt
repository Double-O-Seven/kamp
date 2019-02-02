package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import java.util.LinkedList
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerMapIconIdAllocator
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnPlayerConnectListener, OnPlayerDisconnectListener {

    private var playerMapIconIds: MutableMap<Player, LinkedList<PlayerMapIconId>> = mutableMapOf()

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerConnect(player: Player) {
        playerMapIconIds[player] = LinkedList<PlayerMapIconId>().apply {
            (0..99).forEach { add(PlayerMapIconId.valueOf(it)) }
        }
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        playerMapIconIds.remove(player)
    }

    fun allocate(player: Player): Allocation {
        val playerMapIconId = playerMapIconIds[player]?.pollFirst()
                ?: throw IllegalStateException("Failed to allocate player map icon ID")
        return Allocation(player, playerMapIconId)
    }

    inner class Allocation(private val player: Player, val playerMapIconId: PlayerMapIconId) {

        private var isReleased: Boolean = false

        fun release() {
            if (isReleased) {
                throw IllegalStateException("Player map icon ID ${playerMapIconId.value} was already released")
            }
            playerMapIconIds[player]?.addFirst(playerMapIconId)
            isReleased = true
        }
    }

}