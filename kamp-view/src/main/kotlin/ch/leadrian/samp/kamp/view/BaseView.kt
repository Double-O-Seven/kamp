package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.Player

open class BaseView(player: Player, layoutCalculator: ViewLayoutCalculator) : View(player, layoutCalculator) {

    override fun draw(layout: Rectangle) {}

    override fun onShow() {}

    override fun onHide() {}
}