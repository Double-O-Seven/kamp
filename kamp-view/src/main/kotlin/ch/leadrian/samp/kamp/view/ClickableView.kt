package ch.leadrian.samp.kamp.view

import ch.leadrian.samp.kamp.core.api.entity.Player
import kotlin.reflect.full.cast

abstract class ClickableView(player: Player, layoutCalculator: ViewLayoutCalculator) : View(player, layoutCalculator) {

    private val onClickListeners: MutableList<OnClickViewListener> = mutableListOf()

    val isEnabled: Boolean = true

    protected fun click() {
        if (isEnabled) {
            onClickListeners.forEach { it.onClick(this) }
        }
    }

    fun addOnClickListener(listener: OnClickViewListener) {
        onClickListeners += listener
    }

    fun removeOnClickListener(listener: OnClickViewListener) {
        onClickListeners -= listener
    }

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
