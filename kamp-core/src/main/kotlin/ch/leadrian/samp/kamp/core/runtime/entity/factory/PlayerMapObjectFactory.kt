package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapObject
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject

internal class PlayerMapObjectFactory
@Inject
constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun create(
            player: Player,
            modelId: Int,
            coordinates: Vector3D,
            rotation: Vector3D,
            drawDistance: Float
    ): PlayerMapObject {
        val playerMapObject = PlayerMapObject(
                player = player,
                model = modelId,
                coordinates = coordinates,
                rotation = rotation,
                drawDistance = drawDistance,
                nativeFunctionExecutor = nativeFunctionExecutor
        )
        player.playerMapObjectRegistry.register(playerMapObject)
        playerMapObject.onDestroy { player.playerMapObjectRegistry.unregister(this) }
        return playerMapObject
    }

}