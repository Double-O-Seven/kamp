package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.view.View

internal data class ViewNavigationElement(
        val view: View,
        val isManualNavigationAllowed: Boolean,
        private val destroyOnPop: Boolean,
        private val useMouse: Boolean,
        private val hoverColor: Color
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