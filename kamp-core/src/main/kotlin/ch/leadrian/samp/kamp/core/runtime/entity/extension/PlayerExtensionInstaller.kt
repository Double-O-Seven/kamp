package ch.leadrian.samp.kamp.core.runtime.entity.extension

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerConnectListener
import ch.leadrian.samp.kamp.core.api.callback.Priority
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.extension.EntityExtensionFactory
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Priority(value = Int.MAX_VALUE, listenerClass = OnPlayerConnectListener::class)
@Singleton
internal class PlayerExtensionInstaller
@Inject
constructor(
        private val playerExtensionFactories: Set<@JvmSuppressWildcards EntityExtensionFactory<Player, *>>,
        private val callbackListenerManager: CallbackListenerManager
) : OnPlayerConnectListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerConnect(player: Player) {
        playerExtensionFactories.forEach {
            player.extensions.install(it)
        }
    }

}