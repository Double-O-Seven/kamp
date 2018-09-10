package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextLabel
import ch.leadrian.samp.kamp.core.api.entity.Vehicle
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerTextLabelId
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class PlayerTextLabelImpl(
        coordinates: Vector3D,
        text: String,
        color: ch.leadrian.samp.kamp.core.api.data.Color,
        override val drawDistance: Float,
        override val testLOS: Boolean,
        override val player: Player,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        attachToPlayer: Player?,
        attachToVehicle: Vehicle?
) : PlayerTextLabel {

    private val onDestroyHandlers: MutableList<PlayerTextLabelImpl.() -> Unit> = mutableListOf()

    override val id: PlayerTextLabelId
        get() = requireNotDestroyed { field }

    init {
        val playerTextLabelId = nativeFunctionExecutor.createPlayer3DTextLabel(
                playerid = player.id.value,
                DrawDistance = drawDistance,
                testLOS = testLOS,
                color = color.value,
                text = text,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                attachedplayer = attachToPlayer?.id?.value ?: SAMPConstants.INVALID_PLAYER_ID,
                attachedvehicle = attachToVehicle?.id?.value ?: SAMPConstants.INVALID_VEHICLE_ID
        )

        if (playerTextLabelId == SAMPConstants.INVALID_3DTEXT_ID) {
            throw CreationFailedException("Failed to create player 3D text label")
        }

        id = PlayerTextLabelId.valueOf(playerTextLabelId)
    }

    private var _text: String = text

    override var text: String
        get() = _text
        set(value) {
            nativeFunctionExecutor.updatePlayer3DTextLabelText(
                    playerid = player.id.value,
                    id = id.value,
                    text = value,
                    color = _color.value
            )
            _text = value
        }

    private var _color: ch.leadrian.samp.kamp.core.api.data.Color = color.toColor()

    override var color: ch.leadrian.samp.kamp.core.api.data.Color
        get() = _color
        set(value) {
            nativeFunctionExecutor.updatePlayer3DTextLabelText(
                    playerid = player.id.value,
                    id = id.value,
                    text = _text,
                    color = value.value
            )
            _color = value
        }

    override val coordinates: Vector3D = coordinates.toVector3D()

    override fun update(text: String, color: ch.leadrian.samp.kamp.core.api.data.Color) {
        nativeFunctionExecutor.updatePlayer3DTextLabelText(
                playerid = player.id.value,
                id = id.value,
                text = text,
                color = color.value
        )
        _color = color
        _text = text
    }

    internal fun onDestroy(onDestroy: PlayerTextLabelImpl.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.deletePlayer3DTextLabel(playerid = player.id.value, id = id.value)
        isDestroyed = true
    }
}