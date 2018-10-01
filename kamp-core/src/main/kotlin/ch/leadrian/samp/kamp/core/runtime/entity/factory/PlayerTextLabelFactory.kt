package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject

internal class PlayerTextLabelFactory
@Inject
constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun create(
            player: Player,
            coordinates: Vector3D,
            text: String,
            color: Color,
            drawDistance: Float,
            testLOS: Boolean,
            attachToPlayer: Player?,
            attachToVehicle: Vehicle?
    ): PlayerTextLabel {
        val playerTextLabel = PlayerTextLabel(
                player = player,
                coordinates = coordinates,
                text = text,
                color = color,
                drawDistance = drawDistance,
                testLOS = testLOS,
                nativeFunctionExecutor = nativeFunctionExecutor,
                attachToPlayer = attachToPlayer,
                attachToVehicle = attachToVehicle
        )
        player.playerTextLabelRegistry.register(playerTextLabel)
        playerTextLabel.onDestroy { player.playerTextLabelRegistry.unregister(this) }
        return playerTextLabel
    }

}