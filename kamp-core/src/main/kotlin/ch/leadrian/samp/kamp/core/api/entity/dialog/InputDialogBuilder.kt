package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface InputDialogBuilder : DialogBuilder<InputDialogBuilder> {

    infix fun message(text: String): InputDialogBuilder

    infix fun message(textKey: TextKey): InputDialogBuilder

    infix fun message(supplier: (Player) -> String): InputDialogBuilder

    infix fun message(supplier: DialogTextSupplier): InputDialogBuilder

    infix fun validator(validator: DialogInputValidator): InputDialogBuilder

    fun validators(vararg validator: DialogInputValidator): InputDialogBuilder

    infix fun isPasswordInput(isPasswordInput: Boolean): InputDialogBuilder

    infix fun isPasswordInput(isPasswordInput: Dialog.(Player) -> Boolean): InputDialogBuilder

    infix fun onSubmit(onSubmit: Dialog.(Player, String) -> Unit): InputDialogBuilder

    infix fun onInvalidInput(onInvalidInput: Dialog.(Player, Any) -> Unit): InputDialogBuilder

    infix fun onCancel(onSubmit: Dialog.(Player) -> Unit): InputDialogBuilder

}