package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.PlayerMarkersMode
import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.constants.WeaponModel
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerRegistry
import ch.leadrian.samp.kamp.core.runtime.entity.registry.PlayerSearchIndex
import javax.inject.Inject

class PlayerService
@Inject
internal constructor(
        private val playerRegistry: PlayerRegistry,
        private val playerSearchIndex: PlayerSearchIndex,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun isPlayerConnected(playerId: PlayerId): Boolean = playerRegistry[playerId] != null

    fun getPlayer(playerId: PlayerId): Player =
            playerRegistry[playerId] ?: throw NoSuchEntityException("No player with ID ${playerId.value}")

    fun getAllPlayers(): List<Player> = playerRegistry.getAll()

    fun enableStuntBonusForAll() {
        nativeFunctionExecutor.enableStuntBonusForAll(true)
    }

    fun disableStuntBonusForAll() {
        nativeFunctionExecutor.enableStuntBonusForAll(false)
    }

    fun getMaxPlayers(): Int = nativeFunctionExecutor.getMaxPlayers()

    fun getPoolSize(): Int = nativeFunctionExecutor.getPlayerPoolSize()

    fun showNameTags() {
        nativeFunctionExecutor.showNameTags(true)
    }

    fun hideNameTags() {
        nativeFunctionExecutor.showNameTags(false)
    }

    fun showMarkers(mode: PlayerMarkersMode) {
        nativeFunctionExecutor.showPlayerMarkers(mode.value)
    }

    fun allowInteriorWeapons() {
        nativeFunctionExecutor.allowInteriorWeapons(true)
    }

    fun forbidInteriorWeapons() {
        nativeFunctionExecutor.allowInteriorWeapons(false)
    }

    fun setDeathDropAmount(amount: Int) {
        nativeFunctionExecutor.setDeathDropAmount(amount)
    }

    fun enableZoneNames() {
        nativeFunctionExecutor.enableZoneNames(true)
    }

    fun disableZoneNames() {
        nativeFunctionExecutor.enableZoneNames(false)
    }

    fun usePlayerPedAnimations() {
        nativeFunctionExecutor.usePlayerPedAnims()
    }

    fun setNameTagDrawDistance(distance: Float) {
        nativeFunctionExecutor.setNameTagDrawDistance(distance)
    }

    fun disableNameTagLineOfSight() {
        nativeFunctionExecutor.disableNameTagLOS()
    }

    fun limitGlobalChatRadius(radius: Float) {
        nativeFunctionExecutor.limitGlobalChatRadius(radius)
    }

    fun limitPlayerMarkerRadius(radius: Float) {
        nativeFunctionExecutor.limitPlayerMarkerRadius(radius)
    }

    fun getPlayerByName(name: String): Player? = playerSearchIndex.getPlayer(name)

    fun findPlayersByName(name: String): List<Player> = playerSearchIndex.findPlayers(name)

    fun sendDeathMessage(victim: Player, weapon: WeaponModel, killer: Player? = null) {
        nativeFunctionExecutor.sendDeathMessage(
                killer = killer?.id?.value ?: SAMPConstants.INVALID_PLAYER_ID,
                killee = victim.id.value,
                weapon = weapon.value
        )
    }
}