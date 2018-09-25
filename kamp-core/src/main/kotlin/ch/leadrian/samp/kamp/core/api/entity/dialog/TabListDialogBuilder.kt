package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface TabListDialogBuilder<V : Any> : DialogBuilder<TabListDialogBuilder<V>> {

    fun headerContent(vararg text: String): TabListDialogBuilder<V>

    fun headerContent(vararg textKey: TextKey): TabListDialogBuilder<V>

    fun headerContent(vararg supplier: (Player) -> String): TabListDialogBuilder<V>

    fun headerContent(vararg supplier: DialogTextSupplier): TabListDialogBuilder<V>

    infix fun item(builder: TabListDialogItem.Builder<V>.() -> Unit): TabListDialogBuilder<V>

    infix fun item(item: TabListDialogItem<V>): TabListDialogBuilder<V>

    fun items(vararg item: TabListDialogItem<V>): TabListDialogBuilder<V>

    infix fun items(items: Collection<TabListDialogItem<V>>): TabListDialogBuilder<V>

    infix fun onCancel(onCancel: Dialog.(Player) -> Unit): TabListDialogBuilder<V>

    infix fun onSelectItem(onSelectItem: Dialog.(Player, TabListDialogItem<V>, String) -> Unit): TabListDialogBuilder<V>

}