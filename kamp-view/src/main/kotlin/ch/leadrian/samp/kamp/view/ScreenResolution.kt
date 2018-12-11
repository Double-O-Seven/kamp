package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.entity.Player

data class ScreenResolution(var width: Int = 1920, var height: Int = 1080)

val Player.screenResolution: ScreenResolution
    get() = extensions[ScreenResolution::class]
