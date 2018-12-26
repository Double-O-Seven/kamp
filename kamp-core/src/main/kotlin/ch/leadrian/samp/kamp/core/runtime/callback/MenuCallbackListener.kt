package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerExitedMenuListener
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowListener
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MenuCallbackListener
@Inject
constructor(
        private val callbackListenerManager: CallbackListenerManager
) : OnPlayerExitedMenuListener, OnPlayerSelectedMenuRowListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerExitedMenu(player: Player) {
        player.menu?.onExit(player)
    }

    override fun onPlayerSelectedMenuRow(player: Player, row: MenuRow) {
        row.onSelected(player)
    }
}