package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.api.constants.MapIconType
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor

internal class PlayerMapIconImpl internal constructor(
        override val player: PlayerImpl,
        override val id: PlayerMapIconId,
        private val nativeFunctionsExecutor: SAMPNativeFunctionExecutor,
        coordinates: Vector3D,
        type: MapIconType,
        color: Color,
        style: MapIconStyle
) : PlayerMapIcon {

    override var coordinates: Vector3D = coordinates
        set(value) {
            requireNotDestroyed()
            field = value.toVector3D()
            update()
        }

    override var type: MapIconType = type
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    override var color: Color = color
        set(value) {
            requireNotDestroyed()
            field = value.toColor()
            update()
        }

    override var style: MapIconStyle = style
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    init {
        update()
    }

    private fun update() {
        nativeFunctionsExecutor.setPlayerMapIcon(
                playerid = player.id.value,
                iconid = id.value,
                x = coordinates.x,
                y = coordinates.y,
                z = coordinates.z,
                color = color.value,
                style = style.value,
                markertype = type.value
        )
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        isDestroyed = true
        player.unregisterMapIcon(this)
        if (player.isOnline) {
            nativeFunctionsExecutor.removePlayerMapIcon(playerid = player.id.value, iconid = id.value)
        }
    }
}