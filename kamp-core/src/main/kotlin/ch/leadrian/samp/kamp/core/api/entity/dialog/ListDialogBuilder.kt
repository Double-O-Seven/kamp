package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.entity.Player

interface ListDialogBuilder<V : Any> : DialogBuilder<ListDialogBuilder<V>> {

    fun item(builder: ListDialogItem.Builder<V>.() -> Unit): ListDialogBuilder<V>

    fun item(item: ListDialogItem<V>): ListDialogBuilder<V>

    fun items(vararg item: ListDialogItem<V>): ListDialogBuilder<V>

    fun items(items: Collection<ListDialogItem<V>>): ListDialogBuilder<V>

    fun onCancel(onCancel: Dialog.(Player) -> OnDialogResponseListener.Result): ListDialogBuilder<V>

    fun onSelectItem(onSelectItem: Dialog.(Player, ListDialogItem<V>, String) -> Unit): ListDialogBuilder<V>

}