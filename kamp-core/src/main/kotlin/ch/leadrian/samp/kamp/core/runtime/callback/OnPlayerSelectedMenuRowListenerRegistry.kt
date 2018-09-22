package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerSelectedMenuRowListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnPlayerSelectedMenuRowListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnPlayerSelectedMenuRowListener>(OnPlayerSelectedMenuRowListener::class), OnPlayerSelectedMenuRowListener {

    override fun onPlayerSelectedMenuRow(player: ch.leadrian.samp.kamp.core.api.entity.Player, menuRow: ch.leadrian.samp.kamp.core.api.entity.MenuRow) {
        getListeners().forEach {
            it.onPlayerSelectedMenuRow(player, menuRow)
        }
    }

}
