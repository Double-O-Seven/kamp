package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.entity.id.DialogId

interface Dialog : Entity<DialogId> {

    override val id: DialogId

    fun show(forPlayer: Player)

}