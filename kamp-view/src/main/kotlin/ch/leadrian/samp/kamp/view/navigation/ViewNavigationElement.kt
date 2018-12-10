package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.view.base.View

internal data class ViewNavigationElement(
        val view: View,
        val allowManualNavigation: Boolean,
        private val destroyOnPop: Boolean,
        val useMouse: Boolean
) {

    fun navigateTo() {
        view.apply {
            show(draw = false)
            draw()
            if (useMouse) {
                player.selectTextDraw(hoverColor)
            }
        }
    }

    fun onPop() {
        if (destroyOnPop) {
            view.destroy()
        } else {
            view.hide()
        }
    }

}