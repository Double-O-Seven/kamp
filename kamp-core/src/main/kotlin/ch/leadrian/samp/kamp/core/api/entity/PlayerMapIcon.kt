package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class PlayerMapIcon
internal constructor(
        override val player: Player,
        override val id: PlayerMapIconId,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        coordinates: Vector3D,
        type: MapIconType,
        color: Color,
        style: MapIconStyle
) : Entity<PlayerMapIconId>, HasPlayer, Destroyable {

    var coordinates: Vector3D = coordinates.toVector3D()
        set(value) {
            requireNotDestroyed()
            field = value.toVector3D()
            update()
        }

    var type: MapIconType = type
        set(value) {
            requireNotDestroyed()
            field = value
            update()
        }

    var color: Color = color
        set(value) {
            requireNotDestroyed()
            field = value.toColor()
            update()
        }

    var style: MapIconStyle = style
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