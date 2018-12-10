package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.view.SCREEN_AREA
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.layout.ViewDimension
import ch.leadrian.samp.kamp.view.layout.ViewLayout
import ch.leadrian.samp.kamp.view.layout.pixels
import java.util.*
import java.util.Collections.unmodifiableSet

open class View(
        override val player: Player,
        private val viewContext: ViewContext
) : AbstractDestroyable(), HasPlayer {

    private val _children: LinkedHashSet<View> = LinkedHashSet()

    private lateinit var layout: ViewLayout

    val parentArea: Rectangle
        get() = parent?.contentArea ?: SCREEN_AREA

    val marginArea: Rectangle
        get() = layout.marginArea

    val paddingArea: Rectangle
        get() = layout.paddingArea

    val contentArea: Rectangle
        get() = layout.contentArea

    var parent: View? = null
        private set

    val children: Collection<View> = unmodifiableSet(_children)

    var isHidden: Boolean = false
        private set

    var isInvalidated: Boolean = true
        private set

    var width: ViewDimension? = null

    var height: ViewDimension? = null

    var left: ViewDimension? = null

    var right: ViewDimension? = null

    var top: ViewDimension? = null

    var bottom: ViewDimension? = null

    var paddingLeft: ViewDimension = 0.pixels()

    var paddingRight: ViewDimension = 0.pixels()

    var paddingTop: ViewDimension = 0.pixels()

    var paddingBottom: ViewDimension = 0.pixels()

    var marginLeft: ViewDimension = 0.pixels()

    var marginRight: ViewDimension = 0.pixels()

    var marginTop: ViewDimension = 0.pixels()

    var marginBottom: ViewDimension = 0.pixels()

    var hoverColor: Color = Colors.CYAN

    infix fun leftToLeftOf(view: View) {
        left = pixels { view.marginArea.minX - this.parentArea.minX }
    }

    infix fun leftToRightOf(view: View) {
        left = pixels { view.marginArea.maxX - this.parentArea.minX }
    }

    infix fun rightToLeftOf(view: View) {
        right = pixels { this.parentArea.maxX - view.marginArea.minX }
    }

    infix fun rightToRightOf(view: View) {
        right = pixels { this.parentArea.maxX - view.marginArea.maxX }
    }

    infix fun topToTopOf(view: View) {
        top = pixels { view.marginArea.minY - this.parentArea.minY }
    }

    infix fun topToBottomOf(view: View) {
        top = pixels { view.marginArea.maxY - this.parentArea.minY }
    }

    infix fun bottomToTopOf(view: View) {
        bottom = pixels { this.parentArea.maxY - view.marginArea.minY }
    }

    infix fun bottomToBottomOf(view: View) {
        bottom = pixels { this.parentArea.maxY - view.marginArea.maxY }
    }

    fun setPadding(value: ViewDimension) {
        paddingLeft = value
        paddingRight = value
        paddingTop = value
        paddingBottom = value
    }

    fun setHorizontalPadding(value: ViewDimension) {
        paddingLeft = value
        paddingRight = value
    }

    fun setVerticalPadding(value: ViewDimension) {
        paddingTop = value
        paddingBottom = value
    }

    fun setMargin(value: ViewDimension) {
        marginLeft = value
        marginRight = value
        marginTop = value
        marginBottom = value
    }

    fun setHorizontalMargin(value: ViewDimension) {
        marginLeft = value
        marginRight = value
    }

    fun setVerticalMargin(value: ViewDimension) {
        marginTop = value
        marginBottom = value
    }

    fun draw() {
        requireNotDestroyed()
        if (!isHidden) {
            drawNonRecursive()
        }

        _children.forEach { it.draw() }
    }

    private fun drawNonRecursive() {
        if (isInvalidated) {
            calculateLayout()
        }
        onDraw()
        isInvalidated = false
    }

    protected open fun onDraw() {}

    private fun calculateLayout() {
        layout = viewContext.viewLayoutCalculator.calculate(this)
    }

    fun invalidate() {
        requireNotDestroyed()
        isInvalidated = true
        _children.forEach { it.invalidate() }
    }

    fun show(draw: Boolean = true, invalidate: Boolean = true) {
        requireNotDestroyed()
        if (isHidden) {
            isHidden = false
            if (invalidate) {
                isInvalidated = true
            }
            onShow()
            if (draw) {
                drawNonRecursive()
            }
        }
        _children.forEach { it.show(draw = draw, invalidate = invalidate) }
    }

    protected open fun onShow() {}

    fun hide() {
        requireNotDestroyed()
        if (!isHidden) {
            isHidden = true
            onHide()
        }
        _children.forEach { it.hide() }
    }

    protected open fun onHide() {}

    fun addChild(view: View) {
        requireNotDestroyed()
        if (view.parent == this) {
            return
        }

        view.parent?.removeChild(view)
        _children += view
        view.parent = this
    }

    fun addChildren(children: Iterable<View>) {
        children.forEach { addChild(it) }
    }

    fun addChildren(vararg children: View) {
        children.forEach { addChild(it) }
    }

    fun removeChild(view: View) {
        requireNotDestroyed()
        if (view.parent != this) {
            return
        }

        _children -= view
        view.parent = null
    }

    final override var isDestroyed: Boolean = false
        private set

    final override fun destroy() {
        if (isDestroyed) {
            return
        }

        _children.forEach { it.destroy() }
        _children.clear()
        onDestroy()
        isDestroyed = true
    }

    protected open fun onDestroy() {}

}