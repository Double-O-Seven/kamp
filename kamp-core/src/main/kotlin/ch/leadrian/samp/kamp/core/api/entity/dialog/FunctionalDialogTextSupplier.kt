package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player

class FunctionalDialogTextSupplier(val function: (Player) -> String) : DialogTextSupplier {

    override fun getText(player: Player): String = function(player)

}