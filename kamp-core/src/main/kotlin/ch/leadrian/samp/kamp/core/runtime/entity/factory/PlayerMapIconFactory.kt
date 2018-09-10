package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject

internal class PlayerMapIconFactory
@Inject
constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun create(
            player: Player,
            playerMapIconId: PlayerMapIconId,
            coordinates: Vector3D,
            type: MapIconType,
            color: Color,
            style: MapIconStyle
    ): PlayerMapIcon = PlayerMapIcon(
            player = player,
            id = playerMapIconId,
            nativeFunctionExecutor = nativeFunctionExecutor,
            coordinates = coordinates,
            type = type,
            color = color,
            style = style
    )
}