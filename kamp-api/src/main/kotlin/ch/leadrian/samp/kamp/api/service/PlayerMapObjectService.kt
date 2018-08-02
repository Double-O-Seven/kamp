package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapObjectId

interface PlayerMapObjectService {

    fun createPlayerMapObject(
            playerId: PlayerId,
            modelId: Int,
            coordinates: Vector3D,
            rotation: Vector3D,
            drawDistance: Float = 0f
    ): PlayerMapObject

    fun isValid(playerId: PlayerId, mapObjectId: PlayerMapObjectId): Boolean

    fun getPlayerMapObject(playerId: PlayerId, mapObjectId: PlayerMapObjectId): PlayerMapObject

    fun getAllPlayerMapObjects(playerId: PlayerId): List<PlayerMapObject>

}