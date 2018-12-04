package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.view.View
import javax.inject.Inject

internal class ViewNavigationElementFactory
@Inject
constructor() {

    fun create(
            view: View,
            allowManualNavigation: Boolean,
            useMouse: Boolean,
            destroyOnPop: Boolean,
            hoverColor: Color
    ): ViewNavigationElement = ViewNavigationElement(
            view = view,
            isManualNavigationAllowed = allowManualNavigation,
            useMouse = useMouse,
            hoverColor = hoverColor,
            destroyOnPop = destroyOnPop
    )

}