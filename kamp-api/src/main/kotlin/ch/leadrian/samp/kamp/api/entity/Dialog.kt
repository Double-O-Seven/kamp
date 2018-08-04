package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.id.DialogId

interface Dialog {

    val id: DialogId

    fun show(forPlayer: Player)

}