package ch.leadrian.samp.kamp.api.callback

import ch.leadrian.samp.kamp.api.entity.Player

interface OnEnterExitModShopListener {

    fun onEnterExitModShop(player: Player, entered: Boolean, interiorId: Int)

}
