package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.data.MutableRectangle
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.data.mutableRectangleOf
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.entity.Destroyable
import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import java.util.*

abstract class View(override val player: Player) : HasPlayer, Destroyable {

    companion object {

        val SCREEN_VIEW_PORT = rectangleOf(0f, 640f, 0f, 480f)

    }

    private val _children: LinkedHashSet<View> = LinkedHashSet()

    private lateinit var viewPort: Rectangle

    private val dimensions: ViewDimensions = ViewDimensions()

    var parent: View? = null
        private set

    // val children: Collection<View> = unmodifiableSet(_children)

    var isHidden: Boolean = false
        private set

    var isInvalidated: Boolean = true
        private set

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

    fun draw() {
        requireNotDestroyed()
        if (isHidden) {
            return
        }

        drawWithoutChildren()
        _children.forEach { it.draw() }
    }

    private fun drawWithoutChildren() {
        if (isInvalidated) {
            measure()
        }
        draw(viewPort)
        isInvalidated = false
    }

    protected abstract fun draw(viewPort: Rectangle)

    private fun measure() {
        // TODO calculate view port
        val newViewPort = mutableRectangleOf(0f, 0f, 0f, 0f)
        onMeasure(newViewPort)
        viewPort = newViewPort.toRectangle()
    }

    protected open fun onMeasure(viewPort: MutableRectangle) {}

    fun invalidate() {
        isInvalidated = true
        _children.forEach { it.invalidate() }
    }

    fun show(draw: Boolean = true) {
        if (!isHidden) {
            return
        }

        isHidden = false
        onShow()
        if (draw) {
            drawWithoutChildren()
        }
        _children.forEach { it.show(draw) }
    }

    protected abstract fun onShow()

    fun hide() {
        if (isHidden) {
            return
        }

        isHidden = true
        onHide()
        _children.forEach { it.hide() }
    }

    protected abstract fun onHide()

    fun addChild(view: View) {
        check(view != this)
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
        if (view.parent != this) {
            return
        }

        _children -= view
        view.parent = null
    }

}