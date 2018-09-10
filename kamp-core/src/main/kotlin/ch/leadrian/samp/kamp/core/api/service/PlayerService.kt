package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.constants.PlayerMarkersMode
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId

interface PlayerService {

    fun getPlayer(playerId: PlayerId): Player

    fun getAllPlayers(): List<Player>

    fun enableStuntBonusForAll()

    fun getMaxPlayers(): Int

    fun getPoolSize(): Int

    fun showNameTags(show: Boolean)

    fun showMarkers(mode: PlayerMarkersMode)

    fun allowInteriorWeapons(allow: Boolean)

    fun allowAdminTeleport(allow: Boolean)

    fun setDeathDropAmount(amount: Int)

    fun enableZoneNames(enable: Boolean)

    fun usePlayerPedAnimations()

    fun setNameTagDrawDistance(distance: Float)

    fun disableNameTagLineOfSight()

    fun limitGlobalChatRadius(radius: Float)

    fun limitPlayerMarkerRadius(radius: Float)

    fun getPlayerByName(name: String): Player?

    fun findPlayersByName(name: String): List<Player>
}