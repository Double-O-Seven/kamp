package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowListener
import ch.leadrian.samp.kamp.core.api.entity.MenuRow
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectedMenuRowListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectedMenuRowListener>(OnPlayerSelectedMenuRowListener::class), OnPlayerSelectedMenuRowListener {

    override fun onPlayerSelectedMenuRow(player: Player, menuRow: MenuRow) {
        listeners.forEach {
            it.onPlayerSelectedMenuRow(player, menuRow)
        }
    }

}
