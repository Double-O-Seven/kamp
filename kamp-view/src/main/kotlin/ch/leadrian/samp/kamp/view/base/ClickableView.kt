package ch.leadrian.samp.kamp.view.base

import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickPlayerTextDrawListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.view.ViewContext
import kotlin.reflect.full.cast

abstract class ClickableView(player: Player, viewContext: ViewContext) : View(player, viewContext) {

    private val onClickListeners = LinkedHashSet<OnClickViewListener>()

    var isEnabled: Boolean = false
        private set

    fun click(): OnPlayerClickPlayerTextDrawListener.Result {
        if (isEnabled) {
            onClickListeners.forEach { it.onClick(this) }
            return OnPlayerClickPlayerTextDrawListener.Result.Processed
        }
        return OnPlayerClickPlayerTextDrawListener.Result.NotFound
    }

    fun addOnClickListener(listener: OnClickViewListener) {
        onClickListeners += listener
    }

    fun removeOnClickListener(listener: OnClickViewListener) {
        onClickListeners -= listener
    }

    fun enable() {
        if (isEnabled) {
            return
        }

        isEnabled = true
        onEnable()
    }

    protected open fun onEnable() {}

    fun disable() {
        if (!isEnabled) {
            return
        }

        isEnabled = false
        onDisable()
    }

    protected open fun onDisable() {}

}

inline fun <reified T : ClickableView> T.onClick(crossinline onClick: T.() -> Unit): OnClickViewListener {
    val listener = object : OnClickViewListener {

        override fun onClick(view: View) {
            onClick.invoke(T::class.cast(view))
        }

    }
    addOnClickListener(listener)
    return listener
}
