package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface TabListDialogBuilder<V : Any> : DialogBuilder<TabListDialogBuilder<V>> {

    fun headerContent(vararg text: String): TabListDialogBuilder<V>

    fun headerContent(vararg textKey: TextKey): TabListDialogBuilder<V>

    fun headerContent(vararg supplier: (Player) -> String): TabListDialogBuilder<V>

    fun headerContent(vararg supplier: DialogTextSupplier): TabListDialogBuilder<V>

    fun item(builder: TabListDialogItem.Builder<V>.() -> Unit): TabListDialogBuilder<V>

    fun item(item: TabListDialogItem<V>): TabListDialogBuilder<V>

    fun items(vararg item: TabListDialogItem<V>): TabListDialogBuilder<V>

    fun items(items: Collection<TabListDialogItem<V>>): TabListDialogBuilder<V>

    fun onCancel(onCancel: Dialog.(Player) -> OnDialogResponseListener.Result): TabListDialogBuilder<V>

    fun onSelectItem(onSelectItem: Dialog.(Player, TabListDialogItem<V>, String) -> Unit): TabListDialogBuilder<V>

}