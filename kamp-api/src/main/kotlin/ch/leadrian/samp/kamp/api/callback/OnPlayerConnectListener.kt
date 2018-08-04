package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnPlayerConnectListener {

    fun onPlayerConnect(player: Player): Boolean

}
