package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.text.TextKey

interface InputDialogBuilder : DialogBuilder<InputDialogBuilder> {

    fun message(text: String): InputDialogBuilder

    fun message(textKey: TextKey): InputDialogBuilder

    fun message(supplier: (Player) -> String): InputDialogBuilder

    fun message(supplier: DialogTextSupplier): InputDialogBuilder

    fun passwordInput(passwordInput: Boolean): InputDialogBuilder

    fun onSubmit(onSubmit: Dialog.(Player, String) -> Unit): InputDialogBuilder

    fun onCancel(onSubmit: Dialog.(Player) -> Unit): InputDialogBuilder

}