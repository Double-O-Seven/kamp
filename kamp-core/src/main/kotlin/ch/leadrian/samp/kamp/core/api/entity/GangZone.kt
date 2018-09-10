package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.data.Rectangle
import ch.leadrian.samp.kamp.core.api.entity.id.GangZoneId

interface GangZone : Destroyable, Entity<GangZoneId> {

    override val id: GangZoneId

    val area: Rectangle

    fun show(forPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color)

    fun showForAll(color: ch.leadrian.samp.kamp.core.api.data.Color)

    fun hide(forPlayer: Player)

    fun hideForAll()

    fun flash(forPlayer: Player, color: ch.leadrian.samp.kamp.core.api.data.Color)

    fun flashForAll(color: ch.leadrian.samp.kamp.core.api.data.Color)

    fun stopFlash(forPlayer: Player)

    fun stopFlashForAll()

}