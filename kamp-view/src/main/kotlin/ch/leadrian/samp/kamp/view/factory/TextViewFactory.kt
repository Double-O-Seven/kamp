package ch.leadrian.samp.kamp.view.factory

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.TextView
import ch.leadrian.samp.kamp.view.View

interface TextViewFactory : ViewFactory {

    val textProvider: TextProvider

    val textFormatter: TextFormatter

    val playerTextDrawService: PlayerTextDrawService

    fun textView(player: Player, buildingBlock: TextView.() -> Unit): TextView {
        val textView = TextView(player, viewAreaCalculator, textProvider, textFormatter, playerTextDrawService)
        buildingBlock(textView)
        return textView
    }

    fun View.textView(buildingBlock: TextView.() -> Unit): TextView {
        val textView = textView(player, buildingBlock)
        addChild(textView)
        return textView
    }

}
