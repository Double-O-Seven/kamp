package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.TextLabelId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class TextLabel
internal constructor(
        coordinates: Vector3D,
        text: String,
        color: Color,
        val virtualWorldId: Int,
        val drawDistance: Float,
        val testLOS: Boolean,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<TextLabelId>, AbstractDestroyable() {

    private val onDestroyHandlers: MutableList<TextLabel.() -> Unit> = mutableListOf()

    override val id: TextLabelId
        get() = requireNotDestroyed { field }

    init {
        val textLabelId = nativeFunctionExecutor.create3DTextLabel(
                DrawDistance = drawDistance,
                testLOS = testLOS,
                color = color.value,
                text = text,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                virtualworld = virtualWorldId
        )

        if (textLabelId == SAMPConstants.INVALID_3DTEXT_ID) {
            throw CreationFailedException("Failed to create player 3D text label")
        }

        id = TextLabelId.valueOf(textLabelId)
    }

    private var _text: String = text

    var text: String
        get() = _text
        set(value) {
            nativeFunctionExecutor.update3DTextLabelText(
                    id = id.value,
                    text = value,
                    color = _color.value
            )
            _text = value
        }

    private var _color: Color = color.toColor()

    var color: Color
        get() = _color
        set(value) {
            nativeFunctionExecutor.update3DTextLabelText(
                    id = id.value,
                    text = _text,
                    color = value.value
            )
            _color = value
        }

    val coordinates: Vector3D = coordinates.toVector3D()

    fun update(text: String, color: Color) {
        nativeFunctionExecutor.update3DTextLabelText(
                id = id.value,
                text = text,
                color = color.value
        )
        _color = color
        _text = text
    }

    fun attachTo(player: Player, offset: Vector3D) {
        nativeFunctionExecutor.attach3DTextLabelToPlayer(
                id = id.value,
                playerid = player.id.value,
                OffsetX = offset.x,
                OffsetY = offset.y,
                OffsetZ = offset.z
        )
    }

    fun attachTo(vehicle: Vehicle, offset: Vector3D) {
        nativeFunctionExecutor.attach3DTextLabelToVehicle(
                id = id.value,
                vehicleid = vehicle.id.value,
                OffsetX = offset.x,
                OffsetY = offset.y,
                OffsetZ = offset.z
        )
    }

    @JvmSynthetic
    internal fun onDestroy(onDestroy: TextLabel.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.delete3DTextLabel(id.value)
        isDestroyed = true
    }
}