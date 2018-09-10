package ch.leadrian.samp.kamp.core.api.callback

import ch.leadrian.samp.kamp.core.api.entity.Player

interface OnEnterExitModShopListener {

    fun onEnterExitModShop(player: Player, entered: Boolean, interiorId: Int)

}
