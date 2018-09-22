package ch.leadrian.samp.kamp.core.runtime.callback

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerRegistry
import ch.leadrian.samp.kamp.core.api.callback.OnEnterExitModShopListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OnEnterExitModShopListenerRegistry
@Inject
constructor() : CallbackListenerRegistry<OnEnterExitModShopListener>(OnEnterExitModShopListener::class), OnEnterExitModShopListener {

    override fun onEnterExitModShop(player: ch.leadrian.samp.kamp.core.api.entity.Player, entered: kotlin.Boolean, interiorId: kotlin.Int) {
        getListeners().forEach {
            it.onEnterExitModShop(player, entered, interiorId)
        }
    }

}
