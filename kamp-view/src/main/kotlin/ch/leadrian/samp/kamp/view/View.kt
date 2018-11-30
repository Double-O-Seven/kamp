package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.MutableRectangle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import java.util.*
import java.util.Collections.unmodifiableSet

abstract class View(
        override val player: Player,
        private val layoutCalculator: ViewLayoutCalculator
) : HasPlayer, Destroyable {

    private val _children: LinkedHashSet<View> = LinkedHashSet()

    lateinit var layout: Rectangle

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
            measure()
        }
        draw(layout)
        isInvalidated = false
    }

    protected abstract fun draw(layout: Rectangle)

    private fun measure() {
        val newLayout = layoutCalculator.calculate(this)
        onMeasure(newLayout)
        layout = newLayout.toRectangle()
    }

    protected open fun onMeasure(layout: MutableRectangle) {}

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
        _children.forEach { it.show(draw, invalidate) }
    }

    protected abstract fun onShow()

    fun hide() {
        requireNotDestroyed()
        if (!isHidden) {
            isHidden = true
            onHide()
        }
        _children.forEach { it.hide() }
    }

    protected abstract fun onHide()

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