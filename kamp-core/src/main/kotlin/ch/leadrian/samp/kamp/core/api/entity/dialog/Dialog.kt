package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Entity
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId

interface Dialog : Entity<DialogId> {

    override val id: DialogId

    fun show(forPlayer: Player)

}