package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.entity.Player

open class BaseView(player: Player, areaCalculator: ViewAreaCalculator) : View(player, areaCalculator) {

    override fun onDraw() {}

}