package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.text.TextKey

interface MessageBoxDialogBuilder : DialogBuilder<MessageBoxDialogBuilder> {

    fun message(text: String): MessageBoxDialogBuilder

    fun message(textKey: TextKey): MessageBoxDialogBuilder

    fun message(supplier: (Player) -> String): MessageBoxDialogBuilder

    fun message(supplier: DialogTextSupplier): MessageBoxDialogBuilder

    fun onClickLeftButton(onClickLeftButton: Dialog.(Player) -> Unit): MessageBoxDialogBuilder

    fun onClickRightButton(onClickLeftButton: Dialog.(Player) -> Unit): MessageBoxDialogBuilder

}