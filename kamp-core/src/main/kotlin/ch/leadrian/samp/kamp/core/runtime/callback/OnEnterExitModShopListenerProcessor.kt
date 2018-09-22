package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnEnterExitModShopListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnEnterExitModShopListenerProcessor
@Inject
constructor() : CallbackListenerRegistry<OnEnterExitModShopListener>(OnEnterExitModShopListener::class), OnEnterExitModShopListener {

    override fun onEnterExitModShop(player: Player, entered: Boolean, interiorId: Int) {
        listeners.forEach {
            it.onEnterExitModShop(player, entered, interiorId)
        }
    }

}
