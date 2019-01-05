package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerCancelTextDrawSelectionListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ViewNavigator
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerCancelTextDrawSelectionListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerCancelTextDrawSelection(player: Player): OnPlayerCancelTextDrawSelectionListener.Result {
        with(player.viewNavigation) {
            if (!isMouseUsed) {
                return OnPlayerCancelTextDrawSelectionListener.Result.Ignored
            }

            if (isManualNavigationAllowed) {
                pop()
            } else {
                top?.let { player.selectTextDraw(it.hoverColor) }
            }
            return OnPlayerCancelTextDrawSelectionListener.Result.Processed
        }
    }

}