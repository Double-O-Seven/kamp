package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface DialogBuilder<B : DialogBuilder<B>> {

    infix fun caption(text: String): B

    infix fun caption(textKey: TextKey): B

    infix fun caption(supplier: (Player) -> String): B

    infix fun caption(supplier: DialogTextSupplier): B

    infix fun leftButton(text: String): B

    infix fun leftButton(textKey: TextKey): B

    infix fun leftButton(supplier: (Player) -> String): B

    infix fun leftButton(supplier: DialogTextSupplier): B

    infix fun rightButton(text: String): B

    infix fun rightButton(textKey: TextKey): B

    infix fun rightButton(supplier: (Player) -> String): B

    infix fun rightButton(supplier: DialogTextSupplier): B

    fun build(): Dialog

}