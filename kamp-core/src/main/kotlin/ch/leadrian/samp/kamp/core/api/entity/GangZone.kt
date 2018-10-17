package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

class GangZone
internal constructor(
        area: Rectangle,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : Entity<GangZoneId>, AbstractDestroyable() {

    private val onDestroyHandlers: MutableList<GangZone.() -> Unit> = mutableListOf()

    override val id: GangZoneId
        get() = requireNotDestroyed { field }

    init {
        val gangZoneId = nativeFunctionExecutor.gangZoneCreate(
                minx = area.minX,
                maxx = area.maxX,
                miny = area.minY,
                maxy = area.maxY
        )

        if (gangZoneId == SAMPConstants.INVALID_GANG_ZONE) {
            throw CreationFailedException("Could not create gang zone")
        }

        id = GangZoneId.valueOf(gangZoneId)
    }

    val area: Rectangle = area.toRectangle()

    fun show(forPlayer: Player, color: Color) {
        nativeFunctionExecutor.gangZoneShowForPlayer(
                zone = id.value,
                playerid = forPlayer.id.value,
                color = color.value
        )
    }

    fun showForAll(color: Color) {
        nativeFunctionExecutor.gangZoneShowForAll(zone = id.value, color = color.value)
    }

    fun hide(forPlayer: Player) {
        nativeFunctionExecutor.gangZoneHideForPlayer(zone = id.value, playerid = forPlayer.id.value)
    }

    fun hideForAll() {
        nativeFunctionExecutor.gangZoneHideForAll(id.value)
    }

    fun flash(forPlayer: Player, color: Color) {
        nativeFunctionExecutor.gangZoneFlashForPlayer(
                zone = id.value,
                playerid = forPlayer.id.value,
                flashcolor = color.value
        )
    }

    fun flashForAll(color: Color) {
        nativeFunctionExecutor.gangZoneFlashForAll(zone = id.value, flashcolor = color.value)
    }

    fun stopFlash(forPlayer: Player) {
        nativeFunctionExecutor.gangZoneStopFlashForPlayer(zone = id.value, playerid = forPlayer.id.value)
    }

    fun stopFlashForAll() {
        nativeFunctionExecutor.gangZoneStopFlashForAll(id.value)
    }

    fun onDestroy(onDestroy: GangZone.() -> Unit) {
        onDestroyHandlers += onDestroy
    }

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if (isDestroyed) return

        onDestroyHandlers.forEach { it.invoke(this) }
        nativeFunctionExecutor.gangZoneDestroy(id.value)
        isDestroyed = true
    }
}