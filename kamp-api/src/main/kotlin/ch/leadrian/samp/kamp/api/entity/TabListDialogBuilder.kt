package ch.leadrian.samp.kamp.api.entity

import ch.leadrian.samp.kamp.api.text.TextKey

interface TabListDialogBuilder<V> : DialogBuilder<TabListDialogBuilder<V>> {

    fun headerContent(vararg text: String): TabListDialogBuilder<V>

    fun headerContent(vararg textKey: TextKey): TabListDialogBuilder<V>

    fun headerContent(vararg supplier: (Player) -> String): TabListDialogBuilder<V>

    fun headerContent(vararg supplier: DialogTextSupplier): TabListDialogBuilder<V>

    fun item(builder: TabListDialogItem.Builder<V>.() -> Unit): TabListDialogBuilder<V>

    fun item(item: TabListDialogItem<V>): TabListDialogBuilder<V>

    fun items(vararg item: TabListDialogItem<V>): TabListDialogBuilder<V>

    fun items(items: Collection<TabListDialogItem<V>>): TabListDialogBuilder<V>

    fun onCancel(onCancel: Dialog.(Player) -> Unit): TabListDialogBuilder<V>

    fun onSelectItem(onSelectItem: Dialog.(Player, TabListDialogItem<V>) -> Unit): TabListDialogBuilder<V>

}