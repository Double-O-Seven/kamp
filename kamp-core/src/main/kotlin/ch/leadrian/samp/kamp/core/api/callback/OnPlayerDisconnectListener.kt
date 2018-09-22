package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnPlayerDisconnectListener {

    fun onPlayerDisconnect(player: Player, reason: DisconnectReason)

}
