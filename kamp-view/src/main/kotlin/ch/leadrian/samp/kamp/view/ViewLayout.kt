package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.Rectangle

internal data class ViewLayout(
        val marginArea: Rectangle,
        val paddingArea: Rectangle,
        val contentArea: Rectangle
)