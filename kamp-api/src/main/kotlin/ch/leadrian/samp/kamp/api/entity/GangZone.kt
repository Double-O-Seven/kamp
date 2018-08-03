package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.data.Color
import ch.leadrian.samp.kamp.api.data.Rectangle
import ch.leadrian.samp.kamp.api.entity.id.GangZoneId

interface GangZone : Destroyable {

    val id: GangZoneId

    val area: Rectangle

    fun show(forPlayer: Player, color: Color)

    fun showForAll(color: Color)

    fun hide(forPlayer: Player)

    fun hideForAll()

    fun flash(forPlayer: Player, color: Color)

    fun flashForAll(color: Color)

    fun stopFlash(forPlayer: Player, color: Color)

    fun stopFlashForAll(color: Color)

}