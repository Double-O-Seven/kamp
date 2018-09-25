package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface MessageBoxDialogBuilder : DialogBuilder<MessageBoxDialogBuilder> {

    infix fun message(text: String): MessageBoxDialogBuilder

    infix fun message(textKey: TextKey): MessageBoxDialogBuilder

    infix fun message(supplier: (Player) -> String): MessageBoxDialogBuilder

    infix fun message(supplier: DialogTextSupplier): MessageBoxDialogBuilder

    infix fun onClickLeftButton(onClickLeftButton: Dialog.(Player) -> Unit): MessageBoxDialogBuilder

    infix fun onClickRightButton(onClickRightButton: Dialog.(Player) -> Unit): MessageBoxDialogBuilder

}