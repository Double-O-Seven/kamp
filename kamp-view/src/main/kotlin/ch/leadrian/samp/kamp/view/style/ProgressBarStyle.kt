package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors

interface ProgressBarStyle : Style {

    @JvmDefault
    val progressBarPrimaryColor: Color
        get() = Colors.RED

    @JvmDefault
    val progressBarSecondaryColor: Color
        get() = Colors.DARK_RED

    @JvmDefault
    val progressBarOutlineColor: Color
        get() = Colors.BLACK

}