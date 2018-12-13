package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.view.base.View

internal data class ViewNavigationElement(
        val view: View,
        val allowManualNavigation: Boolean,
        val useMouse: Boolean
) {

    fun navigateTo() {
        view.apply {
            show(draw = false)
            if (useMouse) {
                player.selectTextDraw(hoverColor)
            } else {
                player.cancelSelectTextDraw()
            }
            draw()
        }
    }

}