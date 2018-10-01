package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapObjectId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerMapObjectFactory
import javax.inject.Inject

class PlayerMapObjectService
@Inject
internal constructor(
        private val playerMapObjectFactory: PlayerMapObjectFactory,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) {

    fun createPlayerMapObject(
            player: Player,
            modelId: Int,
            coordinates: Vector3D,
            rotation: Vector3D,
            drawDistance: Float = 0f
    ): PlayerMapObject = playerMapObjectFactory.create(
            player = player,
            modelId = modelId,
            coordinates = coordinates,
            rotation = rotation,
            drawDistance = drawDistance
    )

    fun isValidPlayerMapObject(player: Player, playerMapObjectId: PlayerMapObjectId): Boolean =
            nativeFunctionExecutor.isValidPlayerObject(playerid = player.id.value, objectid = playerMapObjectId.value)

    fun getPlayerMapObject(player: Player, mapObjectId: PlayerMapObjectId): PlayerMapObject =
            player.playerMapObjectRegistry[mapObjectId] ?: throw NoSuchEntityException(
                    "No player map object for player ID ${player.id.value} and ID ${mapObjectId.value}"
            )

    fun getAllPlayerMapObjects(player: Player): List<PlayerMapObject> = player.playerMapObjectRegistry.getAll()

}