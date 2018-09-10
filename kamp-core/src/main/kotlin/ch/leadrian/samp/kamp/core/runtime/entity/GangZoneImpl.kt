package ch.leadrian.samp.kamp.core.runtime.entity

import ch.leadrian.samp.kamp.core.api.constants.SAMPConstants
import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.GangZone
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId
import ch.leadrian.samp.kamp.core.api.entity.requireNotDestroyed
import ch.leadrian.samp.kamp.core.api.exception.CreationFailedException
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor

internal class GangZoneImpl(
        area: Rectangle,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : GangZone, AbstractDestroyable() {

    private val onDestroyHandlers: MutableList<GangZoneImpl.() -> Unit> = mutableListOf()

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

    override val area: Rectangle = area.toRectangle()

    override fun show(forPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color) {
        nativeFunctionExecutor.gangZoneShowForPlayer(
                zone = id.value,
                playerid = forPlayer.id.value,
                color = color.value
        )
    }

    override fun showForAll(color: ch.leadrian.samp.kamp.core.api.data.Color) {
        nativeFunctionExecutor.gangZoneShowForAll(zone = id.value, color = color.value)
    }

    override fun hide(forPlayer: Player) {
        nativeFunctionExecutor.gangZoneHideForPlayer(zone = id.value, playerid = forPlayer.id.value)
    }

    override fun hideForAll() {
        nativeFunctionExecutor.gangZoneHideForAll(id.value)
    }

    override fun flash(forPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color) {
        nativeFunctionExecutor.gangZoneFlashForPlayer(
                zone = id.value,
                playerid = forPlayer.id.value,
                flashcolor = color.value
        )
    }

    override fun flashForAll(color: ch.leadrian.samp.kamp.core.api.data.Color) {
        nativeFunctionExecutor.gangZoneFlashForAll(zone = id.value, flashcolor = color.value)
    }

    override fun stopFlash(forPlayer: Player) {
        nativeFunctionExecutor.gangZoneStopFlashForPlayer(zone = id.value, playerid = forPlayer.id.value)
    }

    override fun stopFlashForAll() {
        nativeFunctionExecutor.gangZoneStopFlashForAll(id.value)
    }

    internal fun onDestroy(onDestroy: GangZoneImpl.() -> Unit) {
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