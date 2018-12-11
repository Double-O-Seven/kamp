package ch.leadrian.samp.kamp.view.screenresolution

import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.Player

class PlayerScreenResolution
internal constructor(
        override val player: Player,
        private val screenResolutionDialogProvider: ScreenResolutionDialogProvider,
        var width: Int = 1920,
        var height: Int = 1080
) : HasPlayer {

    fun set(screenResolution: ScreenResolution) {
        width = screenResolution.width
        height = screenResolution.height
    }

    fun showSelectionDialog() {
        screenResolutionDialogProvider.get().show(player)
    }

}

val Player.screenResolution: PlayerScreenResolution
    get() = extensions[PlayerScreenResolution::class]
