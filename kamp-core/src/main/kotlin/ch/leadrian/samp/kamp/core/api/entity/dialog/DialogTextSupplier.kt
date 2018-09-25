package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player

interface DialogTextSupplier {

    fun getText(player: Player): String

}