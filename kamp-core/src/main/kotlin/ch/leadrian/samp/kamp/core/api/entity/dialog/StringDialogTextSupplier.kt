package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player

class StringDialogTextSupplier(val text: String) : DialogTextSupplier {

    override fun getText(player: Player): String = text

}