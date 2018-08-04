package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerDisconnectListener {

    fun onPlayerDisconnect(player: Player, reason: DisconnectReason): Boolean

}
