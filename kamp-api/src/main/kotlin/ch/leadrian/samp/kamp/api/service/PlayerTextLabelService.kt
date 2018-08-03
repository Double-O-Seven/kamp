package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Player
import ch.leadrian.samp.kamp.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.api.entity.Vehicle
import ch.leadrian.samp.kamp.api.entity.id.PlayerTextLabelId
import ch.leadrian.samp.kamp.api.text.TextKey

interface PlayerTextLabelService {

    fun createPlayerTextLabel(
            text: String,
            color: Color,
            coordinates: Vector3D,
            drawDistance: Float,
            testLOS: Boolean = false,
            attachedToPlayer: Player? = null,
            attachedToVehicle: Vehicle? = null
    ): PlayerTextLabel

    fun createPlayerTextLabel(
            textKey: TextKey,
            color: Color,
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