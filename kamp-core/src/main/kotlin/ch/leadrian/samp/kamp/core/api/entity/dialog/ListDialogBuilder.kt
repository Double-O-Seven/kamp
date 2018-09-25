package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player

interface ListDialogBuilder<V : Any> : DialogBuilder<ListDialogBuilder<V>> {

    infix fun item(builder: ListDialogItem.Builder<V>.() -> Unit): ListDialogBuilder<V>

    infix fun item(item: ListDialogItem<V>): ListDialogBuilder<V>

    fun items(vararg item: ListDialogItem<V>): ListDialogBuilder<V>

    infix fun items(items: Collection<ListDialogItem<V>>): ListDialogBuilder<V>

    infix fun onCancel(onCancel: Dialog.(Player) -> Unit): ListDialogBuilder<V>

    infix fun onSelectItem(onSelectItem: Dialog.(Player, ListDialogItem<V>, String) -> Unit): ListDialogBuilder<V>

}