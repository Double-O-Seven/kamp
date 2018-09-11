package ch.leadrian.samp.kamp.core.runtime.entity.factory

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.inject.Inject

internal class PlayerTextDrawFactory
@Inject
constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProvider: TextProvider,
        private val textFormatter: TextFormatter
) {

    fun create(
            player: Player,
            text: String,
            position: Vector2D
    ): PlayerTextDraw {
        val playerTextDraw = PlayerTextDraw(
                player = player,
                text = text,
                position = position,
                nativeFunctionExecutor = nativeFunctionExecutor,
                textProvider = textProvider,
                textFormatter = textFormatter
        )
        player.playerTextDrawRegistry.register(playerTextDraw)
        playerTextDraw.onDestroy { player.playerTextDrawRegistry.unregister(this) }
        return playerTextDraw
    }

}