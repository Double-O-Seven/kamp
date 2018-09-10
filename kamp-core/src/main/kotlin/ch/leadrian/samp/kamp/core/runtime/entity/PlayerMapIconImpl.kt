package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class PlayerMapIconImpl internal constructor(
        override val player: PlayerImpl,
        override val id: PlayerMapIconId,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        coordinates: Vector3D,
        type: ch.leadrian.samp.kamp.core.api.constants.MapIconType,
        color: ch.leadrian.samp.kamp.core.api.data.Color,
        style: ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
) : PlayerMapIcon {

    override var coordinates: Vector3D = coordinates
        set(value) {
            requireNotDestroyed()
            field = value.toVector3D()
            update()
        }

    override var type: ch.leadrian.samp.kamp.core.api.constants.MapIconType = type
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    override var color: ch.leadrian.samp.kamp.core.api.data.Color = color
        set(value) {
            requireNotDestroyed()
            field = value.toColor()
            update()
        }

    override var style: ch.leadrian.samp.kamp.core.api.constants.MapIconStyle = style
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    init {
        update()
    }

    private fun update() {
        nativeFunctionExecutor.setPlayerMapIcon(
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
        if (player.isConnected) {
            nativeFunctionExecutor.removePlayerMapIcon(playerid = player.id.value, iconid = id.value)
        }
    }
}