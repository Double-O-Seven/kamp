package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface DialogBuilder<B : DialogBuilder<B>> {

    fun caption(text: String): B

    fun caption(textKey: TextKey): B

    fun caption(supplier: (Player) -> String): B

    fun caption(supplier: DialogTextSupplier): B

    fun leftButton(text: String): B

    fun leftButton(textKey: TextKey): B

    fun leftButton(supplier: (Player) -> String): B

    fun leftButton(supplier: DialogTextSupplier): B

    fun rightButton(text: String): B

    fun rightButton(textKey: TextKey): B

    fun rightButton(supplier: (Player) -> String): B

    fun rightButton(supplier: DialogTextSupplier): B

    fun build(): Dialog

}