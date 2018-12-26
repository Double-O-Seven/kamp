package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerNameChangeListener {

    fun onPlayerNameChange(player: Player, oldName: String, newName: String)

}
