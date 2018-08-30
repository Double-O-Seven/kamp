package ch.leadrian.samp.kamp.runtime.entity

import ch.leadrian.samp.kamp.api.constants.MapIconStyle
import ch.leadrian.samp.kamp.api.constants.MapIconType
import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PlayerMapIconFactory
@Inject
constructor(private val nativeFunctionsExecutor: SAMPNativeFunctionExecutor) {

    fun create(
            player: PlayerImpl,
            playerMapIconId: PlayerMapIconId,
            coordinates: Vector3D,
            type: MapIconType,
            color: Color,
            style: MapIconStyle
    ): PlayerMapIcon = PlayerMapIconImpl(
            player = player,
            id = playerMapIconId,
            nativeFunctionsExecutor = nativeFunctionsExecutor,
            coordinates = coordinates,
            type = type,
            color = color,
            style = style
    )
}