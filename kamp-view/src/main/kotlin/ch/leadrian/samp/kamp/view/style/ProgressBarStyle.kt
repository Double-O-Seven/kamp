package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors

interface ProgressBarStyle : Style {

    @JvmDefault
    val primaryColor: Color
        get() = Colors.RED

    @JvmDefault
    val secondaryColor: Color
        get() = Colors.DARK_RED

    @JvmDefault
    val outlineColor: Color
        get() = Colors.BLACK

}