package ch.leadrian.samp.kamp.view.factory

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.BackgroundView
import ch.leadrian.samp.kamp.view.ModelView
import ch.leadrian.samp.kamp.view.SpriteView
import ch.leadrian.samp.kamp.view.TextView
import ch.leadrian.samp.kamp.view.View
import ch.leadrian.samp.kamp.view.ViewContext

interface ViewFactory {

    val viewContext: ViewContext

    val textProvider: TextProvider

    val textFormatter: TextFormatter

    val playerTextDrawService: PlayerTextDrawService

    fun view(player: Player, buildingBlock: View.() -> Unit): View {
        val view = View(player, viewContext)
        buildingBlock(view)
        return view
    }

    fun View.view(buildingBlock: View.() -> Unit): View {
        val view = view(player, buildingBlock)
        addChild(view)
        return view
    }

    fun textView(player: Player, buildingBlock: TextView.() -> Unit): TextView {
        val textView = TextView(player, viewContext, textProvider, textFormatter, playerTextDrawService)
        buildingBlock(textView)
        return textView
    }

    fun View.textView(buildingBlock: TextView.() -> Unit): TextView {
        val textView = textView(player, buildingBlock)
        addChild(textView)
        return textView
    }

    fun backgroundView(player: Player, buildingBlock: BackgroundView.() -> Unit): BackgroundView {
        val backgroundView = BackgroundView(player, viewContext, playerTextDrawService)
        buildingBlock(backgroundView)
        return backgroundView
    }

    fun View.backgroundView(buildingBlock: BackgroundView.() -> Unit): BackgroundView {
        val backgroundView = backgroundView(player, buildingBlock)
        addChild(backgroundView)
        return backgroundView
    }

    fun modelView(player: Player, buildingBlock: ModelView.() -> Unit): ModelView {
        val modelView = ModelView(player, viewContext, playerTextDrawService)
        buildingBlock(modelView)
        return modelView
    }

    fun View.modelView(buildingBlock: ModelView.() -> Unit): ModelView {
        val modelView = modelView(player, buildingBlock)
        addChild(modelView)
        return modelView
    }

    fun spriteView(player: Player, buildingBlock: SpriteView.() -> Unit): SpriteView {
        val spriteView = SpriteView(player, viewContext, playerTextDrawService)
        buildingBlock(spriteView)
        return spriteView
    }

    fun View.spriteView(buildingBlock: SpriteView.() -> Unit): SpriteView {
        val spriteView = spriteView(player, buildingBlock)
        addChild(spriteView)
        return spriteView
    }

}