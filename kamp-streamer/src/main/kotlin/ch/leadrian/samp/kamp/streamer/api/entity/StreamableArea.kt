package ch.leadrian.samp.kamp.streamer.api.entity

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerEnterStreamableAreaListener
import ch.leadrian.samp.kamp.streamer.api.callback.OnPlayerLeaveStreamableAreaListener
import kotlin.reflect.full.cast

abstract class StreamableArea {

    private val onEnterListeners = LinkedHashSet<OnPlayerEnterStreamableAreaListener>()
    private val onLeaveListeners = LinkedHashSet<OnPlayerLeaveStreamableAreaListener>()

    fun addOnPlayerEnterStreamableAreaListener(listener: OnPlayerEnterStreamableAreaListener) {
        onEnterListeners += listener
    }

    fun removeOnPlayerEnterStreamableAreaListener(listener: OnPlayerEnterStreamableAreaListener) {
        onEnterListeners -= listener
    }

    fun addOnPlayerLeaveStreamableAreaListener(listener: OnPlayerLeaveStreamableAreaListener) {
        onLeaveListeners += listener
    }

    fun removeOnPlayerLeaveStreamableAreaListener(listener: OnPlayerLeaveStreamableAreaListener) {
        onLeaveListeners -= listener
    }

    abstract operator fun contains(player: Player): Boolean

    protected fun onEnter(player: Player) {
        onEnterListeners.forEach { it.onPlayerEnterStreamableArea(player, this) }
    }

    protected fun onLeave(player: Player) {
        onLeaveListeners.forEach { it.onPlayerLeaveStreamableArea(player, this) }
    }

}

inline fun <reified T : StreamableArea> T.onEnter(crossinline action: T.(Player) -> Unit): OnPlayerEnterStreamableAreaListener {
    val listener = object : OnPlayerEnterStreamableAreaListener {

        override fun onPlayerEnterStreamableArea(player: Player, area: StreamableArea) {
            action(T::class.cast(area), player)
        }

    }
    addOnPlayerEnterStreamableAreaListener(listener)
    return listener
}

inline fun <reified T : StreamableArea> T.onLeave(crossinline action: T.(Player) -> Unit): OnPlayerLeaveStreamableAreaListener {
    val listener = object : OnPlayerLeaveStreamableAreaListener {

        override fun onPlayerLeaveStreamableArea(player: Player, area: StreamableArea) {
            action(T::class.cast(area), player)
        }

    }
    addOnPlayerLeaveStreamableAreaListener(listener)
    return listener
}
