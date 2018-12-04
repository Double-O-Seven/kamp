package ch.leadrian.samp.kamp.view.navigation

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnPlayerClickTextDrawListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.TextDraw
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ViewNavigator
@Inject
constructor(private val callbackListenerManager: CallbackListenerManager) : OnPlayerClickTextDrawListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onPlayerClickTextDraw(player: Player, textDraw: TextDraw?): OnPlayerClickTextDrawListener.Result {
        if (textDraw != null) {
            return OnPlayerClickTextDrawListener.Result.NotFound
        }

        return handleSelectionCancellation(player)
    }

    private fun handleSelectionCancellation(player: Player): OnPlayerClickTextDrawListener.Result {
        with(player.viewNavigation) {
            if (!isMouseUsed) {
                return OnPlayerClickTextDrawListener.Result.NotFound
            }

            if (isManualNavigationAllowed) {
                pop()
            } else {
                top?.let { player.selectTextDraw(it.hoverColor) }
            }
            return OnPlayerClickTextDrawListener.Result.Processed
        }
    }

}