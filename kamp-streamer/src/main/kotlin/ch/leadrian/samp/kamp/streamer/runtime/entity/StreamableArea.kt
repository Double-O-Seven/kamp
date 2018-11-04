package ch.leadrian.samp.kamp.streamer.runtime.entity

import ch.leadrian.samp.kamp.core.api.entity.Player

abstract class StreamableArea<T : StreamableArea<T>> : PlayerStreamable {

    private val onEnterHandlers: MutableList<T.(Player) -> Unit> = mutableListOf()

    private val onLeaveHandlers: MutableList<T.(Player) -> Unit> = mutableListOf()

    override fun onStreamIn(forPlayer: Player) {
        onEnterHandlers.forEach { it.invoke(self, forPlayer) }
    }

    override fun onStreamOut(forPlayer: Player) {
        onLeaveHandlers.forEach { it.invoke(self, forPlayer) }
    }

    fun onEnter(onEnter: T.(Player) -> Unit) {
        onEnterHandlers += onEnter
    }

    fun onLeave(onLeave: T.(Player) -> Unit) {
        onLeaveHandlers += onLeave
    }

    abstract fun contains(player: Player)

    @Suppress("UNCHECKED_CAST")
    protected val self: T
        get() = this as T
}