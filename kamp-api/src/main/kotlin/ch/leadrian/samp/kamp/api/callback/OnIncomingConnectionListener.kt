package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.id.PlayerId

interface OnIncomingConnectionListener {

    fun onIncomingConnection(playerId: PlayerId, ipAddress: String, port: Int): Boolean

}
