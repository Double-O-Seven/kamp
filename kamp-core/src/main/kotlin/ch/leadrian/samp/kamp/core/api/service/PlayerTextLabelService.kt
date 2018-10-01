package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextLabelId
import ch.leadrian.samp.kamp.core.api.exception.NoSuchEntityException
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.entity.factory.PlayerTextLabelFactory
import java.util.*
import javax.inject.Inject

class PlayerTextLabelService
@Inject
internal constructor(
        private val playerTextLabelFactory: PlayerTextLabelFactory,
        private val textProvider: TextProvider
) {

    fun createPlayerTextLabel(
            player: Player,
            text: String,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            testLOS: Boolean = false,
            attachedToPlayer: Player? = null,
            attachedToVehicle: Vehicle? = null
    ): PlayerTextLabel = playerTextLabelFactory.create(
            player = player,
            coordinates = coordinates,
            text = text,
            color = color,
            drawDistance = drawDistance,
            testLOS = testLOS,
            attachToPlayer = attachedToPlayer,
            attachToVehicle = attachedToVehicle
    )

    fun createPlayerTextLabel(
            player: Player,
            textKey: TextKey,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            testLOS: Boolean = false,
            attachedToPlayer: Player? = null,
            attachedToVehicle: Vehicle? = null,
            locale: Locale = Locale.getDefault()
    ): PlayerTextLabel {
        val text = textProvider.getText(locale, textKey)
        return playerTextLabelFactory.create(
                player = player,
                coordinates = coordinates,
                text = text,
                color = color,
                drawDistance = drawDistance,
                testLOS = testLOS,
                attachToPlayer = attachedToPlayer,
                attachToVehicle = attachedToVehicle
        )
    }

    fun isValidPlayerTextLabel(player: Player, playerTextLabelId: PlayerTextLabelId): Boolean =
            player.playerTextLabelRegistry[playerTextLabelId] != null

    fun getPlayerTextLabel(player: Player, playerTextLabelId: PlayerTextLabelId): PlayerTextLabel =
            player.playerTextLabelRegistry[playerTextLabelId] ?: throw NoSuchEntityException(
                    "No player text label for player ID ${player.id.value} and ID ${playerTextLabelId.value}"
            )

    fun getAllPlayerTextLabels(player: Player): List<PlayerTextLabel> = player.playerTextLabelRegistry.getAll()

}