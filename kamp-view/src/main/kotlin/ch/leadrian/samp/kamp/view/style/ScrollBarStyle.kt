package ch.leadrian.samp.kamp.view.style

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors

interface ScrollBarStyle : Style {

    @JvmDefault
    val scrollBarColor: Color
        get() = Colors.GREY

    @JvmDefault
    val scrollBarBackgroundColor: Color
        get() = Colors.WHITE

    @JvmDefault
    val scrollBarButtonColor: Color
        get() = Colors.BLACK

}