package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId

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