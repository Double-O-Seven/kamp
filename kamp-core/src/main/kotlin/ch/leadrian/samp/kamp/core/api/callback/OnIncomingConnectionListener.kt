package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId

interface OnIncomingConnectionListener {

    fun onIncomingConnection(playerId: PlayerId, ipAddress: String, port: Int)

}
