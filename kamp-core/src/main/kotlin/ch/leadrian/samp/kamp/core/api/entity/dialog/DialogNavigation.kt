package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.HasPlayer
import ch.leadrian.samp.kamp.core.api.entity.Player
import java.util.*

class DialogNavigation
internal constructor(override val player: Player) : HasPlayer {

    private val stack = LinkedList<Dialog>()

    val top: Dialog?
        get() = stack.peek()

    val isEmpty: Boolean
        get() = stack.isEmpty()

    val size: Int
        get() = stack.size

    fun push(dialog: Dialog) {
        stack.push(dialog)
        dialog.show(player)
    }

    fun pop() {
        if (stack.isNotEmpty()) {
            stack.pop()
        }
        top?.show(player)
    }

    fun setRoot(dialog: Dialog) {
        clear()
        push(dialog)
    }

    fun replaceTop(dialog: Dialog) {
        if (stack.isNotEmpty()) {
            stack.pop()
        }
        push(dialog)
    }

    fun clear() {
        stack.clear()
    }

}