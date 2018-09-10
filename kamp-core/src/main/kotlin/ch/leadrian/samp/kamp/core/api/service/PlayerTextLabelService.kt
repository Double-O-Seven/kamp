package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextLabelId
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface PlayerTextLabelService {

    fun createPlayerTextLabel(
            text: String,
            color: ch.leadrian.samp.kamp.core.api.data.Color,
            coordinates: Vector3D,
            drawDistance: Float,
            testLOS: Boolean = false,
            attachedToPlayer: Player? = null,
            attachedToVehicle: Vehicle? = null
    ): PlayerTextLabel

    fun createPlayerTextLabel(
            textKey: TextKey,
            color: ch.leadrian.samp.kamp.core.api.data.Color,
            coordinates: Vector3D,
            drawDistance: Float,
            testLOS: Boolean = false,
            attachedToPlayer: Player? = null,
            attachedToVehicle: Vehicle? = null
    ): PlayerTextLabel

    fun exists(playerTextLabelId: PlayerTextLabelId): Boolean

    fun getTextLabel(playerTextLabelId: PlayerTextLabelId): PlayerTextLabel

    fun getAllTextLabels(): List<PlayerTextLabel>

}