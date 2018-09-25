package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider

class TextKeyDialogTextSupplier(
        val textKey: TextKey,
        private val textProvider: TextProvider
) : DialogTextSupplier {

    override fun getText(player: Player): String = textProvider.getText(player.locale, textKey)

}