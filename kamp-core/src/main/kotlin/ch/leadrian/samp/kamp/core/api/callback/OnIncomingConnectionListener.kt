package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.annotations.CallbackListener
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId

@CallbackListener(runtimePackageName = "ch.leadrian.samp.kamp.core.runtime.callback")
interface OnIncomingConnectionListener {

    fun onIncomingConnection(playerId: PlayerId, ipAddress: String, port: Int)

}
