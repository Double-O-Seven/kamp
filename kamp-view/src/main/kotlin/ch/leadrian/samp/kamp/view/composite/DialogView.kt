package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.api.constants.TextDrawAlignment
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.colorOf
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.view.ValueSupplier
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.ClickableView
import ch.leadrian.samp.kamp.view.base.SpriteView
import ch.leadrian.samp.kamp.view.base.TextView
import ch.leadrian.samp.kamp.view.base.View
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.layout.pixels
import ch.leadrian.samp.kamp.view.navigation.viewNavigation
import ch.leadrian.samp.kamp.view.style.DialogStyle
import ch.leadrian.samp.kamp.view.style.Style
import java.util.*

open class DialogView(
        player: Player,
        viewContext: ViewContext,
        private val viewFactory: ViewFactory
) : ClickableView(player, viewContext) {

    private lateinit var contentView: View

    private lateinit var closeButtonView: TextView

    private lateinit var goBackButtonView: SpriteView

    private lateinit var titleTextView: TextView

    var titleBarHeight: ViewDimension = 24.pixels()

    var titleBarPadding: ViewDimension = 2.pixels()

    var isCloseable: Boolean = true

    var isNavigable: Boolean = true

    private var titleBarColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.GREY)

    var titleBarColor: Color by titleBarColorSupplier

    fun titleBarColor(titleBarColorSupplier: () -> Color) {
        this.titleBarColorSupplier.value(titleBarColorSupplier)
    }

    private var titleColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.BLACK)

    var titleColor: Color by titleColorSupplier

    fun titleColor(titleColorSupplier: () -> Color) {
        this.titleColorSupplier.value(titleColorSupplier)
    }

    private var titleBackgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(Colors.BLACK)

    var titleBackgroundColor: Color by titleBackgroundColorSupplier

    fun titleBackgroundColor(titleBackgroundColorSupplier: () -> Color) {
        this.titleBackgroundColorSupplier.value(titleBackgroundColorSupplier)
    }

    private var contentBackgroundColorSupplier: ValueSupplier<Color> = ValueSupplier(colorOf(0x00000080))

    var contentBackgroundColor: Color by contentBackgroundColorSupplier

    fun contentBackgroundColor(contentBackgroundColorSupplier: () -> Color) {
        this.contentBackgroundColorSupplier.value(contentBackgroundColorSupplier)
    }

    var titleFont: TextDrawFont
        get() = titleTextView.font
        set(value) {
            titleTextView.font = value
        }

    var titleOutlineSize: Int
        get() = titleTextView.outlineSize
        set(value) {
            titleTextView.outlineSize = value
        }

    var titleShadowSize: Int
        get() = titleTextView.shadowSize
        set(value) {
            titleTextView.shadowSize = value
        }

    var title: String
        get() = titleTextView.text
        set(value) {
            titleTextView.text = value
        }

    fun setTitle(text: String, vararg args: Any) {
        titleTextView.setText(text, *args)
    }

    fun setTitle(textKey: TextKey) {
        titleTextView.setText(textKey)
    }

    fun setTitle(textKey: TextKey, vararg args: Any) {
        titleTextView.setText(textKey, *args)
    }

    fun title(titleSupplier: (Locale) -> String) {
        titleTextView.text(titleSupplier)
    }

    init {
        with(viewFactory) {
            backgroundView {
                color { contentBackgroundColor }
                val titleBarView = backgroundView {
                    color { titleBarColor }
                    left = 0.pixels()
                    right = 0.pixels()
                    top = 0.pixels()
                    height = pixels { parentValue -> titleBarHeight.getValue(parentValue) }
                    setPadding(pixels { parentValue -> titleBarPadding.getValue(parentValue) })
                    goBackButtonView = spriteView {
                        spriteName = "ld_beat:left"
                        left = 0.pixels()
                        enable()
                        onClick { goBack() }
                    }
                    closeButtonView = textView {
                        text = "x"
                        font = TextDrawFont.BANK_GOTHIC
                        enable()
                        onClick { close() }
                    }
                    titleTextView = textView {
                        marginLeft = 8.pixels()
                        leftToRightOf(goBackButtonView)
                        rightToLeftOf(closeButtonView)
                        color { titleColor }
                        backgroundColor { titleBackgroundColor }
                        alignment = TextDrawAlignment.LEFT
                        font = TextDrawFont.FONT2
                        outlineSize = 0
                        shadowSize = 0
                        letterHeight = 100.percent()
                    }
                }
                contentView = view {
                    topToBottomOf(titleBarView)
                }
            }
        }
    }

    fun content(contentSupplier: ViewFactory.() -> View) {
        val content = contentSupplier(viewFactory)
        contentView.addChild(content)
    }

    private fun close() {
        if (player.viewNavigation.top === this) {
            player.viewNavigation.clear()
        } else {
            hide()
        }
        onClose()
    }

    protected open fun onClose() {}

    private fun goBack() {
        if (player.viewNavigation.top === this) {
            player.viewNavigation.pop()
            onGoBack()
        }
    }

    protected open fun onGoBack() {}

    override fun onDraw() {
        super.onDraw()
        updateCloseButtonVisibility()
        updateGoBackButtonVisibility()
    }

    private fun updateCloseButtonVisibility() {
        if (isCloseButtonVisible) {
            closeButtonView.width = pixels { closeButtonView.parentArea.height }
            closeButtonView.show(draw = false)
        } else {
            closeButtonView.width = 0.pixels()
            closeButtonView.hide()
        }
    }

    private val isCloseButtonVisible: Boolean
        get() = isCloseable && ((player.viewNavigation.top === this && player.viewNavigation.isManualNavigationAllowed) || player.viewNavigation.top !== this)

    private fun updateGoBackButtonVisibility() {
        if (isGoBackButtonVisible) {
            goBackButtonView.width = pixels { goBackButtonView.parentArea.height }
            goBackButtonView.show(draw = false)
        } else {
            goBackButtonView.width = 0.pixels()
            goBackButtonView.hide()
        }
    }

    private val isGoBackButtonVisible: Boolean
        get() = isNavigable && player.viewNavigation.top === this && player.viewNavigation.isManualNavigationAllowed

    override fun applyStyle(style: Style): Boolean {
        if (style is DialogStyle) {
            titleBarHeight = style.dialogTitleBarHeight
            titleBarPadding = style.dialogTitleBarPadding
            titleBarColor = style.dialogTitleBarColor
            titleColor = style.dialogTitleColor
            titleBackgroundColor = style.dialogTitleBackgroundColor
            contentBackgroundColor = style.dialogContentBackgroundColor
            titleOutlineSize = style.dialogTitleOutlineSize
            titleShadowSize = style.dialogTitleShadowSize
            titleFont = style.dialogTitleFont
        }
        contentView.style(style)
        return true
    }

}
