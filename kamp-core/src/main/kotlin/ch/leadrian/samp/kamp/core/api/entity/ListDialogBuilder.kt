package ch.leadrian.samp.kamp.core.api.entity

interface ListDialogBuilder<V> : DialogBuilder<ListDialogBuilder<V>> {

    fun item(builder: ListDialogItem.Builder<V>.() -> Unit): ListDialogBuilder<V>

    fun item(item: ListDialogItem<V>): ListDialogBuilder<V>

    fun items(vararg item: ListDialogItem<V>): ListDialogBuilder<V>

    fun items(items: Collection<ListDialogItem<V>>): ListDialogBuilder<V>

    fun onCancel(onCancel: Dialog.(Player) -> Unit): ListDialogBuilder<V>

    fun onSelectItem(onSelectItem: Dialog.(Player, ListDialogItem<V>) -> Unit): ListDialogBuilder<V>

}