package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.entity.PlayerMapIcon
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerMapIconId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.PlayerImpl
import ch.leadrian.samp.kamp.core.runtime.entity.PlayerMapIconImpl
import javax.inject.Inject

internal class PlayerMapIconFactory
@Inject
constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    fun create(
            player: PlayerImpl,
            playerMapIconId: PlayerMapIconId,
            coordinates: Vector3D,
            type: ch.leadrian.samp.kamp.core.api.constants.MapIconType,
            color: ch.leadrian.samp.kamp.core.api.data.Color,
            style: ch.leadrian.samp.kamp.core.api.constants.MapIconStyle
    ): PlayerMapIcon = PlayerMapIconImpl(
            player = player,
            id = playerMapIconId,
            nativeFunctionExecutor = nativeFunctionExecutor,
            coordinates = coordinates,
            type = type,
            color = color,
            style = style
    )
}