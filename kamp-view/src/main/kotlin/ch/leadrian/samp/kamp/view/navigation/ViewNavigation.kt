package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.AbstractDestroyable
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.view.View
import java.util.*

class ViewNavigation
internal constructor(private val viewNavigationElementFactory: ViewNavigationElementFactory) : AbstractDestroyable() {

    private val stack = LinkedList<ViewNavigationElement>()

    val top: View?
        get() = stack.peek()?.view

    val isEmpty: Boolean
        get() = stack.isEmpty()

    val size: Int
        get() = stack.size

    val isManualNavigationAllowed: Boolean
        get() = stack.peek()?.isManualNavigationAllowed ?: true

    @JvmOverloads
    fun push(view: View, allowManualNavigation: Boolean = true, useMouse: Boolean = true, destroyOnPop: Boolean = true, hoverColor: Color = Colors.CYAN) {
        requireNotDestroyed()
        top?.hide()
        val element = viewNavigationElementFactory.create(
                view = view,
                allowManualNavigation = allowManualNavigation,
                useMouse = useMouse,
                destroyOnPop = destroyOnPop,
                hoverColor = hoverColor
        )
        stack.push(element)
        element.navigateTo()
    }

    @JvmOverloads
    fun setRoot(view: View, allowManualNavigation: Boolean = true, useMouse: Boolean = true, destroyOnPop: Boolean = true, hoverColor: Color = Colors.CYAN) {
        requireNotDestroyed()
        clear()
        push(view = view, allowManualNavigation = allowManualNavigation, useMouse = useMouse, destroyOnPop = destroyOnPop, hoverColor = hoverColor)
    }

    @JvmOverloads
    fun replaceTop(view: View, allowManualNavigation: Boolean = true, useMouse: Boolean = true, destroyOnPop: Boolean = true, hoverColor: Color = Colors.CYAN) {
        requireNotDestroyed()
        removeTop()
        push(view = view, allowManualNavigation = allowManualNavigation, useMouse = useMouse, destroyOnPop = destroyOnPop, hoverColor = hoverColor)
    }

    fun pop() {
        requireNotDestroyed()
        removeTop()
        stack.peek()?.navigateTo()
    }

    private fun removeTop() {
        if (stack.isNotEmpty()) {
            stack.pop().onPop()
        }
    }

    fun clear() {
        stack.reversed().forEach { it.onPop() }
        stack.clear()
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) {
            return
        }

        stack.forEach { it.view.destroy() }
        stack.clear()
        isDestroyed = true
    }

}

val Player.viewNavigation: ViewNavigation
    get() = extensions[ViewNavigation::class]