package ch.leadrian.samp.kamp.view.factory

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.service.PlayerTextDrawService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.BackgroundView
import ch.leadrian.samp.kamp.view.base.ModelView
import ch.leadrian.samp.kamp.view.base.SpriteView
import ch.leadrian.samp.kamp.view.base.TextView
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.composite.ButtonView
import ch.leadrian.samp.kamp.view.composite.GridView
import ch.leadrian.samp.kamp.view.composite.GridViewAdapter
import ch.leadrian.samp.kamp.view.composite.HorizontalListView
import ch.leadrian.samp.kamp.view.composite.HorizontalScrollBarView
import ch.leadrian.samp.kamp.view.composite.ListViewAdapter
import ch.leadrian.samp.kamp.view.composite.ModelViewerView
import ch.leadrian.samp.kamp.view.composite.ScrollBarAdapter
import ch.leadrian.samp.kamp.view.composite.VerticalListView
import ch.leadrian.samp.kamp.view.composite.VerticalScrollBarView

interface ViewFactory {

    val viewContext: ViewContext

    val textProvider: TextProvider

    val textFormatter: TextFormatter

    val playerTextDrawService: PlayerTextDrawService

    val dialogService: DialogService

    @JvmDefault
    fun view(player: Player, buildingBlock: View.() -> Unit): View {
        val view = View(player, viewContext)
        buildingBlock(view)
        return view
    }

    @JvmDefault
    fun View.view(buildingBlock: View.() -> Unit): View {
        val view = view(player, buildingBlock)
        addChild(view)
        return view
    }

    @JvmDefault
    fun textView(player: Player, buildingBlock: TextView.() -> Unit): TextView {
        val textView = TextView(player, viewContext, textProvider, textFormatter, playerTextDrawService)
        buildingBlock(textView)
        return textView
    }

    @JvmDefault
    fun View.textView(buildingBlock: TextView.() -> Unit): TextView {
        val textView = textView(player, buildingBlock)
        addChild(textView)
        return textView
    }

    @JvmDefault
    fun backgroundView(player: Player, buildingBlock: BackgroundView.() -> Unit): BackgroundView {
        val backgroundView = BackgroundView(player, viewContext, playerTextDrawService)
        buildingBlock(backgroundView)
        return backgroundView
    }

    @JvmDefault
    fun View.backgroundView(buildingBlock: BackgroundView.() -> Unit): BackgroundView {
        val backgroundView = backgroundView(player, buildingBlock)
        addChild(backgroundView)
        return backgroundView
    }

    @JvmDefault
    fun modelView(player: Player, buildingBlock: ModelView.() -> Unit): ModelView {
        val modelView = ModelView(player, viewContext, playerTextDrawService)
        buildingBlock(modelView)
        return modelView
    }

    @JvmDefault
    fun View.modelView(buildingBlock: ModelView.() -> Unit): ModelView {
        val modelView = modelView(player, buildingBlock)
        addChild(modelView)
        return modelView
    }

    @JvmDefault
    fun spriteView(player: Player, buildingBlock: SpriteView.() -> Unit): SpriteView {
        val spriteView = SpriteView(player, viewContext, playerTextDrawService)
        buildingBlock(spriteView)
        return spriteView
    }

    @JvmDefault
    fun View.spriteView(buildingBlock: SpriteView.() -> Unit): SpriteView {
        val spriteView = spriteView(player, buildingBlock)
        addChild(spriteView)
        return spriteView
    }

    @JvmDefault
    fun buttonView(player: Player, buildingBlock: ButtonView.() -> Unit): ButtonView {
        val buttonView = ButtonView(player, viewContext, this)
        buildingBlock(buttonView)
        return buttonView
    }

    @JvmDefault
    fun View.buttonView(buildingBlock: ButtonView.() -> Unit): ButtonView {
        val buttonView = buttonView(player, buildingBlock)
        addChild(buttonView)
        return buttonView
    }

    @JvmDefault
    fun verticalScrollBarView(player: Player, adapter: ScrollBarAdapter, buildingBlock: VerticalScrollBarView.() -> Unit): VerticalScrollBarView {
        val verticalScrollBarView = VerticalScrollBarView(player, viewContext, this, adapter)
        buildingBlock(verticalScrollBarView)
        return verticalScrollBarView
    }

    @JvmDefault
    fun View.verticalScrollBarView(adapter: ScrollBarAdapter, buildingBlock: VerticalScrollBarView.() -> Unit): VerticalScrollBarView {
        val verticalScrollBarView = verticalScrollBarView(player, adapter, buildingBlock)
        addChild(verticalScrollBarView)
        return verticalScrollBarView
    }

    @JvmDefault
    fun horizontalScrollBarView(player: Player, adapter: ScrollBarAdapter, buildingBlock: HorizontalScrollBarView.() -> Unit): HorizontalScrollBarView {
        val horizontalScrollBarView = HorizontalScrollBarView(player, viewContext, this, adapter)
        buildingBlock(horizontalScrollBarView)
        return horizontalScrollBarView
    }

    @JvmDefault
    fun View.horizontalScrollBarView(adapter: ScrollBarAdapter, buildingBlock: HorizontalScrollBarView.() -> Unit): HorizontalScrollBarView {
        val horizontalScrollBarView = horizontalScrollBarView(player, adapter, buildingBlock)
        addChild(horizontalScrollBarView)
        return horizontalScrollBarView
    }

    @JvmDefault
    fun <T> verticalListView(player: Player, adapter: ListViewAdapter<T>, buildingBlock: VerticalListView<T>.() -> Unit = {}): VerticalListView<T> {
        val verticalListView = VerticalListView(player, viewContext, this, adapter)
        buildingBlock(verticalListView)
        return verticalListView
    }

    @JvmDefault
    fun <T> View.verticalListView(adapter: ListViewAdapter<T>, buildingBlock: VerticalListView<T>.() -> Unit = {}): VerticalListView<T> {
        val verticalListView = verticalListView(player, adapter, buildingBlock)
        addChild(verticalListView)
        return verticalListView
    }

    @JvmDefault
    fun <T> horizontalListView(player: Player, adapter: ListViewAdapter<T>, buildingBlock: HorizontalListView<T>.() -> Unit = {}): HorizontalListView<T> {
        val horizontalListView = HorizontalListView(player, viewContext, this, adapter)
        buildingBlock(horizontalListView)
        return horizontalListView
    }

    @JvmDefault
    fun <T> View.horizontalListView(adapter: ListViewAdapter<T>, buildingBlock: HorizontalListView<T>.() -> Unit = {}): HorizontalListView<T> {
        val horizontalListView = horizontalListView(player, adapter, buildingBlock)
        addChild(horizontalListView)
        return horizontalListView
    }

    @JvmDefault
    fun gridView(player: Player, adapter: GridViewAdapter, buildingBlock: GridView.() -> Unit = {}): GridView {
        val gridView = GridView(player, viewContext, this, adapter)
        buildingBlock(gridView)
        return gridView
    }

    @JvmDefault
    fun View.gridView(adapter: GridViewAdapter, buildingBlock: GridView.() -> Unit = {}): GridView {
        val gridView = gridView(player, adapter, buildingBlock)
        addChild(gridView)
        return gridView
    }

    @JvmDefault
    fun modelViewerView(player: Player, buildingBlock: ModelViewerView.() -> Unit): ModelViewerView {
        val modelViewerView = ModelViewerView(player, viewContext, this)
        buildingBlock(modelViewerView)
        return modelViewerView
    }

    @JvmDefault
    fun View.modelViewerView(buildingBlock: ModelViewerView.() -> Unit): ModelViewerView {
        val modelViewerView = modelViewerView(player, buildingBlock)
        addChild(modelViewerView)
        return modelViewerView
    }

}