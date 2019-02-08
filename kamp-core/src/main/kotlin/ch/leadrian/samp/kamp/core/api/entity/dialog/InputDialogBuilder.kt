package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface InputDialogBuilder : DialogBuilder<InputDialogBuilder> {

    fun message(text: String): InputDialogBuilder

    fun message(textKey: TextKey): InputDialogBuilder

    fun message(supplier: (Player) -> String): InputDialogBuilder

    fun message(supplier: DialogTextSupplier): InputDialogBuilder

    fun validator(validator: DialogInputValidator): InputDialogBuilder

    fun validators(vararg validator: DialogInputValidator): InputDialogBuilder

    fun validators(validators: Collection<DialogInputValidator>): InputDialogBuilder

    fun isPasswordInput(isPasswordInput: Boolean): InputDialogBuilder

    fun isPasswordInput(isPasswordInput: Dialog.(Player) -> Boolean): InputDialogBuilder

    fun onSubmit(onSubmit: Dialog.(Player, String) -> Unit): InputDialogBuilder

    fun onInvalidInput(onInvalidInput: Dialog.(Player, Any) -> Unit): InputDialogBuilder

    fun onCancel(onCancel: Dialog.(Player) -> OnDialogResponseListener.Result): InputDialogBuilder

}