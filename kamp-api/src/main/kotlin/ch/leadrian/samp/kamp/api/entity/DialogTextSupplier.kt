package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.entity.Player

interface DialogTextSupplier {

    fun getText(player: Player): String

}