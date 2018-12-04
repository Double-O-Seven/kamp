package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.view.View
import javax.inject.Inject

internal class ViewNavigationElementFactory
@Inject
constructor() {

    fun create(
            view: View,
            allowManualNavigation: Boolean,
            useMouse: Boolean,
            destroyOnPop: Boolean
    ): ViewNavigationElement = ViewNavigationElement(
            view = view,
            allowManualNavigation = allowManualNavigation,
            useMouse = useMouse,
            destroyOnPop = destroyOnPop
    )

}