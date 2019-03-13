package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerDisconnectListener
import ch.leadrian.samp.kamp.core.api.callback.Priority
import ch.leadrian.samp.kamp.core.api.constants.DisconnectReason
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.id.PlayerId
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Priority(value = Int.MIN_VALUE, listenerClass = OnPlayerDisconnectListener::class)
@Singleton
internal class PlayerRegistry
@Inject
constructor(
        nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val callbackListenerManager: CallbackListenerManager
) : EntityRegistry<Player, PlayerId>(arrayOfNulls(nativeFunctionExecutor.getMaxPlayers())), OnPlayerDisconnectListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerDisconnect(player: Player, reason: DisconnectReason) {
        unregister(player)
    }

}
